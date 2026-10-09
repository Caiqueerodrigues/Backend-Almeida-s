package Development.Rodrigues.Almeidas_Cortes.configs;

import java.io.IOException;
import java.time.Clock;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.HexFormat;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String LIMIT_HEADER = "X-RateLimit-Limit";
    private static final String REMAINING_HEADER = "X-RateLimit-Remaining";

    private final Map<String, RequestHistory> requestsByClient = new ConcurrentHashMap<>();
    private final AtomicLong lastCleanup = new AtomicLong();
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final int maxRequests;
    private final long windowMillis;

    @Autowired
    public RateLimitFilter(
            ObjectMapper objectMapper,
            @Value("${rate-limit.max-requests:100}") int maxRequests,
            @Value("${rate-limit.window-seconds:60}") long windowSeconds) {
        this(objectMapper, maxRequests, windowSeconds, Clock.systemUTC());
    }

    RateLimitFilter(ObjectMapper objectMapper, int maxRequests, long windowSeconds, Clock clock) {
        if (maxRequests < 1 || windowSeconds < 1) {
            throw new IllegalArgumentException("As configurações de rate limit devem ser maiores que zero");
        }
        this.objectMapper = objectMapper;
        this.maxRequests = maxRequests;
        this.windowMillis = windowSeconds * 1_000;
        this.clock = clock;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long now = clock.millis();
        cleanupExpiredClients(now);

        RateLimitDecision decision = requestsByClient
                .computeIfAbsent(clientKey(request), ignored -> new RequestHistory())
                .register(now, windowMillis, maxRequests);

        response.setHeader(LIMIT_HEADER, String.valueOf(maxRequests));
        response.setHeader(REMAINING_HEADER, String.valueOf(decision.remainingRequests()));

        if (!decision.allowed()) {
            long retryAfterSeconds = Math.max(1, (decision.retryAtMillis() - now + 999) / 1_000);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfterSeconds));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ResponseDTO(
                    "",
                    "Muitas requisições. Aguarde " + retryAfterSeconds + " segundos e tente novamente.",
                    "",
                    ""));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String clientKey(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return "token:" + sha256(authorization.substring(7));
        }
        return "ip:" + request.getRemoteAddr();
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não está disponível", ex);
        }
    }

    private void cleanupExpiredClients(long now) {
        long previousCleanup = lastCleanup.get();
        if (now - previousCleanup < windowMillis || !lastCleanup.compareAndSet(previousCleanup, now)) {
            return;
        }
        requestsByClient.entrySet().removeIf(entry -> entry.getValue().isExpired(now, windowMillis));
    }

    private static final class RequestHistory {
        private final Deque<Long> timestamps = new ArrayDeque<>();

        synchronized RateLimitDecision register(long now, long windowMillis, int maxRequests) {
            removeExpired(now, windowMillis);
            if (timestamps.size() >= maxRequests) {
                return new RateLimitDecision(false, 0, timestamps.peekFirst() + windowMillis);
            }
            timestamps.addLast(now);
            return new RateLimitDecision(true, maxRequests - timestamps.size(), 0);
        }

        synchronized boolean isExpired(long now, long windowMillis) {
            removeExpired(now, windowMillis);
            return timestamps.isEmpty();
        }

        private void removeExpired(long now, long windowMillis) {
            long threshold = now - windowMillis;
            while (!timestamps.isEmpty() && timestamps.peekFirst() <= threshold) {
                timestamps.removeFirst();
            }
        }
    }

    private record RateLimitDecision(boolean allowed, int remainingRequests, long retryAtMillis) {
    }
}

package Development.Rodrigues.Almeidas_Cortes.configs;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

class RateLimitFilterTest {

    @Test
    void blocksRequestsOverTheLimitAndInformsWaitingTime() throws Exception {
        MutableClock clock = new MutableClock();
        RateLimitFilter filter = new RateLimitFilter(new ObjectMapper(), 2, 60, clock);

        assertThat(execute(filter, request("10.0.0.1", null)).getStatus()).isEqualTo(200);
        assertThat(execute(filter, request("10.0.0.1", null)).getStatus()).isEqualTo(200);

        MockHttpServletResponse blocked = execute(filter, request("10.0.0.1", null));

        assertThat(blocked.getStatus()).isEqualTo(429);
        assertThat(blocked.getHeader(HttpHeaders.RETRY_AFTER)).isEqualTo("60");
        assertThat(blocked.getContentAsString()).contains("Aguarde 60 segundos");
    }

    @Test
    void allowsClientAgainAfterWindowAndSeparatesAuthenticatedUsers() throws Exception {
        MutableClock clock = new MutableClock();
        RateLimitFilter filter = new RateLimitFilter(new ObjectMapper(), 1, 10, clock);

        assertThat(execute(filter, request("10.0.0.1", "Bearer usuario-a")).getStatus()).isEqualTo(200);
        assertThat(execute(filter, request("10.0.0.1", "Bearer usuario-b")).getStatus()).isEqualTo(200);
        assertThat(execute(filter, request("10.0.0.1", "Bearer usuario-a")).getStatus()).isEqualTo(429);

        clock.advanceSeconds(10);

        assertThat(execute(filter, request("10.0.0.1", "Bearer usuario-a")).getStatus()).isEqualTo(200);
    }

    private MockHttpServletRequest request(String remoteAddress, String authorization) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/models");
        request.setRemoteAddr(remoteAddress);
        if (authorization != null) {
            request.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        }
        return request;
    }

    private MockHttpServletResponse execute(RateLimitFilter filter, MockHttpServletRequest request) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        return response;
    }

    private static final class MutableClock extends Clock {
        private Instant instant = Instant.parse("2026-01-01T00:00:00Z");

        void advanceSeconds(long seconds) {
            instant = instant.plusSeconds(seconds);
        }

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}

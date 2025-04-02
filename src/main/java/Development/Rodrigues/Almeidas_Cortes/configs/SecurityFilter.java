package Development.Rodrigues.Almeidas_Cortes.configs;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;

import Development.Rodrigues.Almeidas_Cortes.services.TokenService;
import Development.Rodrigues.Almeidas_Cortes.users.UserRepository;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); //pegar o subject do token
            var user = repository.findByUser(subject); //buscar o usuario pelo login para dizer que esta logado

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); 

            SecurityContextHolder.getContext().setAuthentication(authentication); //setar o usuario como autenticado
            
            Instant expires = tokenService.getExpirationTime(tokenJWT);
            
            String newAccessToken = tokenService.generateRefreshToken(tokenJWT, expires);
            response.setHeader("Authorization", "Bearer " + newAccessToken); // Retornar novo access token
        }
        
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization"); //pegar o authorization do header
        
        if(authorization != null) {
            return authorization.replace("Bearer ", "");
        }
        
        return null;
    }
}

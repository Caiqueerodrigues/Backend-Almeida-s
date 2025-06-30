package Development.Rodrigues.Almeidas_Cortes.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

@Service
public class TokenService {
    
    @Value("${security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("Almeida's Cortes")
                .withSubject(user.getUser())
                .withClaim("id", user.getId()) //pode ter varios
                .withClaim("user", user.getUser())
                .withClaim("name", user.getName())
                .withClaim("funcao", user.getFunction())
                .withClaim("sexo", user.getSex())
                .withClaim("photo", user.getPhoto())
                .withClaim("refresh_token", dataExpiracao(1)) 
                .withExpiresAt(dataExpiracao(3)) //expiração do token
                .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token");
        }
    }

    private Instant dataExpiracao(Integer hours) {
        return LocalDateTime.now().plusHours(hours).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                .withIssuer("Almeida's Cortes")
                .build()
                .verify(token)
                .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido ou expirado!");
        }
    }
    
    public String generateRefreshToken(String oldToken, Instant expires) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                .withIssuer("Almeida's Cortes")
                .build();
            var decodedJWT = verifier.verify(oldToken);

            String subject = decodedJWT.getSubject();
            long userId = decodedJWT.getClaim("id").asLong();
            String userName = decodedJWT.getClaim("name").asString();
            String userLogin = decodedJWT.getClaim("user").asString();
            String function = decodedJWT.getClaim("funcao").asString();
            String photo = decodedJWT.getClaim("photo").asString();
            String sexo = decodedJWT.getClaim("sexo").asString();
            Instant originalExpiration = decodedJWT.getExpiresAt().toInstant();
            
            return JWT.create()
                .withIssuer("Almeida's Cortes")
                .withSubject(subject)
                .withClaim("id", userId)
                .withClaim("user", userLogin)
                .withClaim("name", userName)
                .withClaim("funcao", function)
                .withClaim("photo", photo)
                .withClaim("sexo", sexo)
                .withClaim("refresh_token", dataExpiracao(1))
                .withExpiresAt(originalExpiration)
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar refresh token");
        }
    }

    public Instant getExpirationTime(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                    .withIssuer("Almeida's Cortes")
                    .build();
            var decodedJWT = verifier.verify(token);
    
            // Retorna a data de expiração do token original
            return decodedJWT.getExpiresAt().toInstant();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recuperar a expiração do token");
        }
    }

    public long getUserIdFromToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var verifier = JWT.require(algorithm)
                    .withIssuer("Almeida's Cortes")
                    .build();
            var decodedJWT = verifier.verify(token);
            
            return decodedJWT.getClaim("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recuperar o ID do usuário do token");
        }
    }
}


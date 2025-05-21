package Development.Rodrigues.Almeidas_Cortes.users;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.services.GetDateHourBrasilia;
import Development.Rodrigues.Almeidas_Cortes.services.TokenService;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private AuthenticationManager manager;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GetDateHourBrasilia dataHoraBrasilia;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO loginService(LoginDTO dados) {
        try {
            
            log.info(passwordEncoder.encode(dados.password())  + " senha tentativa login");
            var token = new UsernamePasswordAuthenticationToken(dados.user(), dados.password());
            var auth = manager.authenticate(token);
    
            User user = (User) auth.getPrincipal();
    
            if (!user.isActive()) {
                return new ResponseDTO("", "Usuário inativo, procure a administração!", "", "");
            }
    
            if(user.getFirstLogin() == null) {
                user.setFirstLogin(dataHoraBrasilia.dataHoraBrasiliaLocalDateTime());
                user.setLastLogin(dataHoraBrasilia.dataHoraBrasiliaLocalDateTime());
            } else {
                user.setLastLogin(dataHoraBrasilia.dataHoraBrasiliaLocalDateTime());
            }
            repository.save(user);
            
            var tokenJWT = tokenService.generateToken(user);
            return new ResponseDTO(tokenJWT, "", "", "");
        } catch (Exception e) {
            log.error("ERRO ao efetuar o login " + e);
            throw new RuntimeException("Erroao efetuar o login, tente novamente.");
        }
    }


}

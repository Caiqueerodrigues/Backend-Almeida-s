package Development.Rodrigues.Almeidas_Cortes.users;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.order.entities.ListOrder;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.services.GetDateHourBrasilia;
import Development.Rodrigues.Almeidas_Cortes.services.TokenService;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;
import Development.Rodrigues.Almeidas_Cortes.users.entities.SendUser;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.servlet.http.HttpServletResponse;

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

    @Value("${backend.api}")
    private String backendApi;

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
            throw new RuntimeException("Erro ao efetuar o login, tente novamente.");
        }
    }

    public ResponseDTO getUserIdService(Long id) {
        try {
            Optional<User> user = repository.findById(id);
    
            if(!user.isEmpty()) {
                User userConfim = user.get();

                if(!userConfim.getPhoto().contains("/images/")) {
                    userConfim.setPhoto(backendApi  + "anexo/perfil/" + userConfim.getPhoto());
                };

                SendUser userSend = new SendUser(
                    userConfim.getName(),
                    userConfim.getUser(),
                    userConfim.isActive(),
                    userConfim.getFullName(), 
                    userConfim.getFunction(),
                    userConfim.getPhoto(),
                    userConfim.getSex(),
                    userConfim.getFirstLogin(),
                    userConfim.getLastLogin()
                );
                return new ResponseDTO(userSend,"","","");
            }
    
            return new ResponseDTO("", "", "", "Dados incorretos");
        } catch (Exception e) {
            log.error("ERRO ao obter o usuário " + e);
            throw new RuntimeException("Erro ao obter o usuário, tente novamente.");
        }
    }
    
    public ResponseDTO updateUserService(
        Long id, 
        MultipartFile photo, 
        String name, 
        String fullName, 
        String funct, 
        String sex, 
        String user, 
        String newPassword, 
        Boolean active,
        HttpServletResponse response
    ) {
        try {
            Optional<User> findUser = repository.findById(id);
            if(findUser.isPresent()) {
                User oldUser = findUser.get();

                if (newPassword != null && !newPassword.trim().isEmpty() && !"null".equalsIgnoreCase(newPassword.trim())) {
                    oldUser.setPassword(passwordEncoder.encode(newPassword));
                }

                oldUser.setName(name);
                oldUser.setFullName(fullName);
                oldUser.setFunction(funct);
                oldUser.setSex(sex);
                oldUser.setUser(user);
                oldUser.setActive(active);

                if (photo != null && !photo.isEmpty()) {
                    String folderPath = "src/main/resources/users";
                    File dir = new File(folderPath);
                    if (!dir.exists()) {
                        dir.mkdirs(); // cria se não existir o diretorio
                    }

                    // 1. Apagar arquivos antigos que começam com o id
                    File[] arquivos = dir.listFiles((dir1, nome) -> nome.startsWith(id.toString() + "_"));
                    if (arquivos != null) {
                        for (File arquivo : arquivos) {
                            arquivo.delete();
                        }
                    }

                    // 2. Salvar novo arquivo
                    String originalFilename = photo.getOriginalFilename().replace(" ", "-");
                    String novoNome = id + "_" + originalFilename;
                    Path destino = Paths.get(folderPath, novoNome);
                    Files.write(destino, photo.getBytes());

                    oldUser.setPhoto(novoNome);
                }

                repository.save(oldUser);

                String newToken = tokenService.generateToken(oldUser);
                response.setHeader("Authorization", "Bearer " + newToken);
            }

            return new ResponseDTO("","","Dados salvos com sucesso!","");
        } catch (Exception e) {
            log.error("ERRO ao alterar o usuário " + e);
            throw new RuntimeException("Erro ao alterar o usuário, tente novamente.");
        }
    }
}

package Development.Rodrigues.Almeidas_Cortes.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.services.TokenService;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginDTO dados ) {
        try {
            var response = service.loginService(dados);

            return ResponseEntity.status(200).body(new ResponseDTO(response, "", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Dados informados Incorretos!", "", ""));
        }
    }
    

}

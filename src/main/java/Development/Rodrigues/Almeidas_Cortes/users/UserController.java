package Development.Rodrigues.Almeidas_Cortes.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
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
            ResponseDTO response = service.loginService(dados);
            return response.response() != "" ? ResponseEntity.status(200).body(response) : ResponseEntity.status(401).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" + e, "", ""));
        }
    }
    

}

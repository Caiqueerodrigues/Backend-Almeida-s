package Development.Rodrigues.Almeidas_Cortes.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.ListAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.VariosAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




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
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserId(@PathVariable Long id) {
        try {
            var response = service.getUserIdService(id);

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
    
    @GetMapping("/not-power")
    public String notPwer() {
            System.out.println("JOB chamou aqui");
            String result = "Tudo Certo";
        return result;
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateUserController (
        @RequestParam(value = "id", required = true) Long id,
        @RequestParam(value = "photo", required = true) MultipartFile photo,
        @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "fullName", required = true) String fullName,
        @RequestParam(value = "funct", required = true) String funct,
        @RequestParam(value = "sex", required = true) String sex,
        @RequestParam(value = "user", required = true) String user,
        @RequestParam(value = "newPassword", required = false) String newPassword,
        @RequestParam(value = "active", required = true) Boolean active
        ) {
        try {
            String originalFilename = photo.getOriginalFilename().replace(" ", "-");
            String novoNome = id + "_" + originalFilename;
            ListAnexosDTO anexo = new ListAnexosDTO(photo, funct, novoNome);

            ResponseDTO response = service.updateUserService(id, photo, name, fullName, funct, sex, user, newPassword, active);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" +e, "", ""));
        }
    }
}

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
import Development.Rodrigues.Almeidas_Cortes.services.TokenService;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginDTO dados, HttpSession session) {
        try {
            var response = service.loginService(dados, session);

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
        @RequestParam(value = "photo", required = false) MultipartFile photo,
        @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "fullName", required = true) String fullName,
        @RequestParam(value = "funct", required = true) String funct,
        @RequestParam(value = "sex", required = true) String sex,
        @RequestParam(value = "user", required = true) String user,
        @RequestParam(value = "newPassword", required = false) String newPassword,
        @RequestParam(value = "active", required = true) Boolean active,
        HttpServletResponse resp
    ) {
        try {
            ResponseDTO response = service.updateUserService(id, photo, name, fullName, funct, sex, user, newPassword, active, resp);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" +e, "", ""));
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity replaceToken(@RequestHeader(value = "Authorization") String authorizationHeader, HttpSession session) {
        try {
            String oldToken = authorizationHeader.substring(7);

            ResponseDTO response = tokenService.replaceToken(oldToken, session);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" +e, "", ""));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        try {
            ResponseDTO response = tokenService.logout(session);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" +e, "", ""));
        }
    }
}

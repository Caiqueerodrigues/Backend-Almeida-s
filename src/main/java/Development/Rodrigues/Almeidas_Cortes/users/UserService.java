package Development.Rodrigues.Almeidas_Cortes.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.users.dto.LoginDTO;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public ResponseDTO loginService(LoginDTO dados) {
        Map<String, String> token = new HashMap<>();
        token.put("token", "KAKSADJSDAKSDJASDKASKD");
        
        // return new ResponseDTO( token, "", "", "");
        return new ResponseDTO( "", "Dados informados incorretos!", "", "");
    }

}

package Development.Rodrigues.Almeidas_Cortes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.users.UserRepository;

@Service //Indica ao Spring que essa classe é um serviço
public class AuthService implements UserDetailsService {
    //UserDetailsService classe do spring própria para autenticacao
    //complete com sugestão de correção da IDE

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUser(username);
    }
    
}
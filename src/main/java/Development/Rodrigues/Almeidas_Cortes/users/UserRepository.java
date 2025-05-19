package Development.Rodrigues.Almeidas_Cortes.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUser(String user);

    Optional<User> findById(Long id);
}

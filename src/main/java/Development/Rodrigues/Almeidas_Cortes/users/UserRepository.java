package Development.Rodrigues.Almeidas_Cortes.users;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

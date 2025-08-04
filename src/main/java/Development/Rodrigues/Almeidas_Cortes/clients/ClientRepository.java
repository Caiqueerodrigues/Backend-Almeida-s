package Development.Rodrigues.Almeidas_Cortes.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);

    Optional<Client> findByNome(String nome);

    List<Client> findByAtivoOrderByNomeAsc(boolean ativo);

    List<Client> findAllByOrderByNomeAsc();
}

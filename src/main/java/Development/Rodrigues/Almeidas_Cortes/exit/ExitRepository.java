package Development.Rodrigues.Almeidas_Cortes.exit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.exit.entities.Exit;
import java.time.LocalDate;


public interface ExitRepository extends JpaRepository<Exit, Long> {
    List<Exit> findByDataCompraAndDeletedIsNull(LocalDate dataCompra);
    Optional<Exit> findByIdAndDeletedIsNull(Long id);

    List<Exit> findByDataCompraBetweenAndDeletedIsNull(LocalDate initialDate, LocalDate finalDate);
}

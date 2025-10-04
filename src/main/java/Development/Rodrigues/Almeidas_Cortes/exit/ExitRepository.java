package Development.Rodrigues.Almeidas_Cortes.exit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.exit.entities.Exit;
import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;

import java.time.LocalDate;


public interface ExitRepository extends JpaRepository<Exit, Long> {
    List<Exit> findByDataCompraAndDeletedIsFalse(LocalDate dataCompra);
    Optional<Exit> findByIdAndDeletedIsFalse(Long id);

    List<Exit> findByDataCompraBetweenAndDeletedIsFalseOrderByDataCompra(LocalDate initialDate, LocalDate finalDate);
    List<Exit> findByDataCompraBetweenAndDeletedIsFalseAndTipoServicoOrderByDataCompra(LocalDate initialDate, LocalDate finalDate, TipoServico tipoServico);
}

package Development.Rodrigues.Almeidas_Cortes.anexos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.Anexo;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {
    List<Anexo> findByIdIn(List<Long> ids);
}

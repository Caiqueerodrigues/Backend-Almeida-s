package Development.Rodrigues.Almeidas_Cortes.tables;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.tables.entities.TableEntity;

public interface TablesRepository extends JpaRepository<TableEntity, Long> {
    List<TableEntity> findAll();

    Optional<TableEntity> findById(Long id);

    List<TableEntity> findByClientId(Long clientId);
}

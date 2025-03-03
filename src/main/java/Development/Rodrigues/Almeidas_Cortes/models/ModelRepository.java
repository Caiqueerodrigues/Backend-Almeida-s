package Development.Rodrigues.Almeidas_Cortes.models;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByClient_Id(Long id);

    Optional<Model> findById(Long id);
}

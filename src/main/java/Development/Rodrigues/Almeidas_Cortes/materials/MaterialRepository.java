package Development.Rodrigues.Almeidas_Cortes.materials;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findAllByOrderByNomeAsc();

    Optional<Material> findById(Long id);

    Optional<Material> findByNome(String nome);
}

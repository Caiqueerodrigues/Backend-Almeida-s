package Development.Rodrigues.Almeidas_Cortes.materials;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findAll();

    Optional<Material> findById(Long id);

    List<Material> findByAtivo(boolean status);

    Optional<Material> findByNome(String nome);
}

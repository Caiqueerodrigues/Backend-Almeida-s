package Development.Rodrigues.Almeidas_Cortes.materials.entities;

import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.UpdateMaterialDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_materials")
@Entity(name = "Materials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String nome;

    @Column
    private boolean ativo;

    public Material(CreateMaterialDTO dados) {
        this.nome = dados.nome();
        this.ativo = dados.ativo();
    }

    public void updateMaterial(UpdateMaterialDTO dados) {
        this.nome = dados.nome();
        this.ativo = dados.ativo();
    }
}
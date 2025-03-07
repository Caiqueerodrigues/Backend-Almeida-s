package Development.Rodrigues.Almeidas_Cortes.models.entities;

import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.dto.CreateModelDTO;
import Development.Rodrigues.Almeidas_Cortes.models.dto.UpdateModelDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_models")
@Entity(name = "Models")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_Client", nullable = false)
    private Client client;

    @Column(name = "tipo_Calcado", length = 100)
    private String tipo;
    
    @Column(name = "preco", nullable = false)
    private Double preco;

    @Column(name = "qtd_Pecas_Par", nullable = false)
    private Long qtdPecasPar;

    @Column(name = "ref_Ordem", length = 100)
    private String refOrdem;

    @Column(name = "fotos", length = 100)
    private String fotos;

    @Column(name = "qtd_Faca", nullable = false)
    private Long qtdFaca;

    @Column(name = "rendimento")
    private String rendimento;

    @Column(name = "cronometragem")
    private String cronometragem;

    @Column(name = "obs", length = 255)
    private String obs;

    public Model(CreateModelDTO dados) {
        this.client = dados.client();
        this.tipo = dados.tipo();
        this.preco = dados.preco();
        this.qtdPecasPar = dados.qtdPecasPar();
        this.refOrdem = dados.ordem();
        this.fotos = dados.fotos();
        this.qtdFaca = dados.qtdFaca();
        this.rendimento = dados.rendimento();
        this.cronometragem = dados.cronometragem();
        this.obs = dados.obs();
    }

    public void updateModel(UpdateModelDTO dados) {
        this.client = dados.client();
        this.tipo = dados.tipo();
        this.preco = dados.preco();
        this.qtdPecasPar = dados.qtdPecasPar();
        this.refOrdem = dados.refOrdem();
        this.fotos = dados.fotos();
        this.qtdFaca = dados.qtdFaca();
        this.rendimento = dados.rendimento();
        this.cronometragem = dados.cronometragem();
        this.obs = dados.obs();
    }
}

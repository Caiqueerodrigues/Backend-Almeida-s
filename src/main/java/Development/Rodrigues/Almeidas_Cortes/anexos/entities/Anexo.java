package Development.Rodrigues.Almeidas_Cortes.anexos.entities;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_anexos")
@Entity(name = "Anexos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_File", nullable = false, length = 100)
    private String nomeFile;

    @ManyToOne
    @JoinColumn(name = "id_Modelo", nullable = false)
    private Model idModelo;
    
    @Column(name = "nome_Peca", nullable = false, length = 100)
    private String nomePeca;
    
    @Column(name = "qtd_Par", nullable = false)
    private Long qtdPar;

    @Column(name = "propriedade_Faca", nullable = false, length = 100)
    private String propriedadeFaca;
    
    @Column(name = "preco_Faca", nullable = false)
    private Double precoFaca;
    
    @Column(name = "obs", nullable = false, length = 255)
    private String obs;

    public Anexo(
        String nomeFile, 
        Model idModelo, 
        String nomePeca, 
        Long qtdPar,
        String propriedadeFaca, 
        Double precoFaca, 
        String obs
    ) {
        this.nomeFile = nomeFile;
        this.idModelo = idModelo;
        this.nomePeca = nomePeca;
        this.qtdPar = qtdPar;
        this.propriedadeFaca = propriedadeFaca;
        this.precoFaca = precoFaca;
        this.obs = obs;
    }
}

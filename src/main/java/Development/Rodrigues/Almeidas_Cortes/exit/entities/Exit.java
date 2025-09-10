package Development.Rodrigues.Almeidas_Cortes.exit.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.exit.dto.UpdateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.CreateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_exit")
@Entity(name = "Exits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Exit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "data_Registro")
    private LocalDateTime dataRegistro;

    @Column(name= "data_Compra")
    private LocalDate dataCompra;
    
    @Column(name= "valor_Compra")
    private Double valorCompra;
    
    @Column(name= "tipo_Servico")
    private TipoServico TipoServico;
    
    @Column(name= "anotacoes")
    private String anotacoes;

    public Exit(CreateExitDTO dados) {
        this.dataRegistro = dados.dataRegistro();
        this.dataCompra = dados.dataCompra();
        this.valorCompra = dados.valorCompra();
        this.TipoServico = dados.TipoServico();
        this.anotacoes = dados.anotacoes();
    }

    public void updateExit(UpdateExitDTO dados) {
        this.dataRegistro = dados.dataRegistro();
        this.dataCompra = dados.dataCompra();
        this.valorCompra = dados.valorCompra();
        this.TipoServico = dados.TipoServico();
        this.anotacoes = dados.anotacoes();
    }
}

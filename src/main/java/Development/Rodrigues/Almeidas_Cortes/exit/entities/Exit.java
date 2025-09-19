package Development.Rodrigues.Almeidas_Cortes.exit.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import Development.Rodrigues.Almeidas_Cortes.exit.dto.UpdateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.CreateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
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
    @Enumerated(EnumType.STRING) 
    private TipoServico TipoServico;
    
    @Column(name= "anotacoes")
    private String anotacoes;
    
    @ManyToOne
    @JoinColumn(name = "id_User")
    private User user;

    public Exit(CreateExitDTO dados, User user) {
        this.dataRegistro = LocalDateTime.now(ZoneId.of("UTC-3"));
        this.dataCompra = dados.dataCompra();
        this.valorCompra = dados.valorCompra();
        this.TipoServico = dados.tipoServico();
        this.anotacoes = dados.anotacoes();
        this.user = user;
    }

    public void updateExit(UpdateExitDTO dados) {
        this.dataCompra = dados.dataCompra();
        this.valorCompra = dados.valorCompra();
        this.TipoServico = dados.tipoServico();
        this.anotacoes = dados.anotacoes();
    }
}

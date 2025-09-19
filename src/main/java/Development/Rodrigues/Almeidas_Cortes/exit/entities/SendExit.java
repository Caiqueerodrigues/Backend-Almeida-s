package Development.Rodrigues.Almeidas_Cortes.exit.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.exit.dto.CreateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.UpdateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "SendExit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SendExit {
    @Id
    private Long id;

    private LocalDateTime dataRegistro;

    private LocalDate dataCompra;
    
    private Double valorCompra;
    
    @Enumerated(EnumType.STRING) 
    private TipoServico TipoServico;
    
    private String anotacoes;
    
    private String user;

    public SendExit(Exit dados) {
        this.id = dados.getId();
        this.dataRegistro = dados.getDataRegistro();
        this.dataCompra = dados.getDataCompra();
        this.valorCompra = dados.getValorCompra();
        this.TipoServico = dados.getTipoServico();
        this.anotacoes = dados.getAnotacoes();
        this.user = dados.getUser().getFullName();
    }
}

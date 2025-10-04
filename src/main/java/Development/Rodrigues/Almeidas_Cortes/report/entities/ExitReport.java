package Development.Rodrigues.Almeidas_Cortes.report.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "ExitReport")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ExitReport {

    @Id
    private Long id;

    private String dataRegistro;

    private String dataCompra;
    
    private Double valorCompra;
    
    private String anotacoes;

    private String nameUser;

    private String diaDaSemana;
}

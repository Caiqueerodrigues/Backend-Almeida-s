package Development.Rodrigues.Almeidas_Cortes.employees.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EmployeeFront")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class EmployeeFront {

    @Id
    private Long id;
    
    private String nomeFuncionario;

    private LocalDate date;

    private String[] horarios;

    private boolean status;

    public EmployeeFront(
        Long id,
        LocalDate date,
        String[] horarios,
        String nomeFuncionario,
        boolean status
        ) {
        this.id = id;
        this.date = date;
        this.horarios = horarios;
        this.nomeFuncionario = nomeFuncionario;
        this.status = status;
    }
}

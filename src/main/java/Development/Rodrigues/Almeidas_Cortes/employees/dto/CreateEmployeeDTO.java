package Development.Rodrigues.Almeidas_Cortes.employees.dto;

import java.time.LocalDate;

public record CreateEmployeeDTO(

    String nomeFuncionario,

    LocalDate date,

    String horarios
) {

}

package Development.Rodrigues.Almeidas_Cortes.employees.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeDTO(

    @NotNull
    Long id, 

    @NotBlank
    String nomeFuncionario,

    LocalDate date,

    @NotBlank
    String horarios,

    boolean status
) {

}

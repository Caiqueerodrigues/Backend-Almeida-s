package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;

public record FilterDateOrdersDTO(
    @PastOrPresent
    @Schema(example = "2025-03-03")
    LocalDate date
) {

}

package Development.Rodrigues.Almeidas_Cortes.order.dto;


import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record FilterDateOrdersDTO(
    @NotNull
    @Schema(example = "2025-03-03 00:00:00")
    LocalDateTime date
) {

}

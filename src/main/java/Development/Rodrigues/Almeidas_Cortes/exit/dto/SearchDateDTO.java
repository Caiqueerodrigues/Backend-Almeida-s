package Development.Rodrigues.Almeidas_Cortes.exit.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SearchDateDTO(
    @NotBlank
    @Schema(example = "2025-09-05")
    String date,

    @Schema(example = "2025-09-05")
    String dateFinal
) {

}

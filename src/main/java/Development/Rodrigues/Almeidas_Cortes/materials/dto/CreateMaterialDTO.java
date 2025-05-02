package Development.Rodrigues.Almeidas_Cortes.materials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMaterialDTO(
    @NotBlank
    @Schema(example = "Forro cacharrel")
    String nome
) {

}

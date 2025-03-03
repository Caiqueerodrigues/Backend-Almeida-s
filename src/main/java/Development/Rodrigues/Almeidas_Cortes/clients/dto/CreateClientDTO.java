package Development.Rodrigues.Almeidas_Cortes.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClientDTO(
    @NotBlank
    @Schema(example = "Flávio da Silva")
    String nome,

    @Schema(example = "Fofi's calçados LTDA")
    String razaoSocial,

    @NotBlank
    @Schema(example = "(18) xxxxx-xxxx")
    String telefone,
    
    @NotNull
    @Schema(example = "true")
    boolean ativo,

    @Schema(example = "Paga todo dia 05")
    String obs
) {

}

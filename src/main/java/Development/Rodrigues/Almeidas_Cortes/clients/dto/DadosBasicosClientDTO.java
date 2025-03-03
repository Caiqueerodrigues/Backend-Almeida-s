package Development.Rodrigues.Almeidas_Cortes.clients.dto;

import jakarta.validation.constraints.*;

public record DadosBasicosClientDTO(
    @NotNull 
    Long id,

    @NotBlank
    String nome,

    @NotBlank 
    String telefone
) {
}

package Development.Rodrigues.Almeidas_Cortes.users.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank
    String user,

    @NotBlank
    String password
) {

}

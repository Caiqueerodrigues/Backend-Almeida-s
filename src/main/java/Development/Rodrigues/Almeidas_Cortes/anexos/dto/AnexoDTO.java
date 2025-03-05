package Development.Rodrigues.Almeidas_Cortes.anexos.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnexoDTO(
    Long id,

    @NotNull
    List<ListAnexosDTO> files,

    @NotBlank
    @Schema(example = "Cabedal, Teste")
    List<String> nomePeca,

    @NotBlank
    @Schema(example = "Teste")
    String nomeModelo,

    @NotNull
    @Schema(example = "1")
    Long idModelo,

    @NotNull
    @Schema(example = "4")
    Long pecaPar,
    
    @NotBlank
    @Schema(example = "Nossa")
    String propriedadeFaca,
    
    @NotNull
    @Schema(example = "0.15")
    Double precoFaca,
    
    @Schema(example = "Cuidado com a faca")
    String obs
) {
}

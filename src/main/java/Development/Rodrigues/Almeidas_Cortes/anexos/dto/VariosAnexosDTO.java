package Development.Rodrigues.Almeidas_Cortes.anexos.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VariosAnexosDTO(
    Long id,

    @NotNull
    List<ListAnexosDTO> files,

    @Schema(example = "1")
    Long idClient,

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
    List<Long> qtdPar,
    
    @NotBlank
    @Schema(example = "Nossa")
    List<String> propriedadeFaca,
    
    @NotNull
    @Schema(example = "0.15, 0.12")
    List<Double> precoFaca,
    
    @Schema(example = "Cuidado com a faca")
    List<String> obs
) {

}

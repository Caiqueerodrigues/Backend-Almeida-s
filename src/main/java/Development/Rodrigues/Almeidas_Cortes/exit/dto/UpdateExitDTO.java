package Development.Rodrigues.Almeidas_Cortes.exit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateExitDTO(
    @Schema(example = "1")
    Long id,

    @Schema(example = "2025-09-09 00:00:00.000")
    LocalDateTime dataRegistro,

    @Schema(example = "2025-09-09")
    LocalDate dataCompra,
    
    @Schema(example = "25.60")
    Double valorCompra,
    
    @Schema(example = "Corte")
    TipoServico TipoServico,
    
    @Schema(example = "O que foi comprado")
    String anotacoes
) {

}

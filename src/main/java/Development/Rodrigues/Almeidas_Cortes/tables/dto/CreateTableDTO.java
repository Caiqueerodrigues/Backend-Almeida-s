package Development.Rodrigues.Almeidas_Cortes.tables.dto;

import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import jakarta.validation.constraints.NotNull;

public record CreateTableDTO(
    
    @NotNull
    Client cliente,

    @NotNull
    List<DinamicTableDTO> tables
) {
}

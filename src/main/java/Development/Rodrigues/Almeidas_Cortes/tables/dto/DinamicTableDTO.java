package Development.Rodrigues.Almeidas_Cortes.tables.dto;

import java.util.List;
import java.util.Map;

public record DinamicTableDTO(
    String id,
    String nome,
    List<ColumnDTO> columns,
    List<Map<String, Object>> rows,
    Boolean isNew
) {

}

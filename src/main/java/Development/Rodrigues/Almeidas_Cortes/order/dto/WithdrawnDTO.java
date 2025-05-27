package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WithdrawnDTO(
    List<Long> ids,
    LocalDateTime dataRetirada,
    String quemRetirou
) {

}

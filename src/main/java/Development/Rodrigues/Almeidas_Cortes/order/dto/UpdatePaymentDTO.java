package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UpdatePaymentDTO(
    List<Long> ids,
    LocalDateTime date
) {

}

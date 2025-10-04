package Development.Rodrigues.Almeidas_Cortes.report.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesFilterReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesSituationReport;
import jakarta.persistence.Enumerated;

public record ParamsFiltersReports(

    @Enumerated
    TypesFilterReport firstFilter,

    Long client,

    List<LocalDateTime> period,

    @Enumerated
    TypesReport report,

    @Enumerated
    TypesSituationReport situation,

    Long idPedido,

    Long quantidadeVias,

    List<Long> idPedidos,

    String category,

    String tipo
) {

}

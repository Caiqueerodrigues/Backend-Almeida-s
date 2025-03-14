package Development.Rodrigues.Almeidas_Cortes.report.dto;

import java.time.LocalDateTime;
import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesFilterReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesSituationReport;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record ParamsFiltersReports(

    @NotNull
    @Enumerated
    TypesFilterReport firstFilter,

    Long client,

    List<LocalDateTime> period,

    @NotNull
    @Enumerated
    TypesReport report,

    @NotNull
    @Enumerated
    TypesSituationReport situation
) {

}

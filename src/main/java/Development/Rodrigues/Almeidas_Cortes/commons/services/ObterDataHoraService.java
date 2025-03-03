package Development.Rodrigues.Almeidas_Cortes.commons.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ObterDataHoraService {
    ZoneId brasiliaZone = ZoneId.of("America/Sao_Paulo");
    
    public String dataBrasilia() {
        LocalDate date = LocalDate.now(brasiliaZone);
        String dataFormatada = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return dataFormatada;
    }

    public String dataHoraBrasilia() {
        LocalDateTime dateComplete = LocalDateTime.now(brasiliaZone);
        String dataFormatada = dateComplete.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        return dataFormatada;
    }
}

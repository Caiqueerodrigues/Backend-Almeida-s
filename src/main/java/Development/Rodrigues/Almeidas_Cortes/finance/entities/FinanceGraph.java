package Development.Rodrigues.Almeidas_Cortes.finance.entities;

import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceGraph {
    private List<String> labels;
    private List<GraphData> dataLine;
    private List<GraphData> dataBar;
    private List<Double> dataPie;
    private List<GraphData> dataPaidBar;
    private List<GraphData> dataReceiveBar;
    private List<DetailsGraphCategory> detailsCategory;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphData {
        private String label;
        private List<Double> data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailsGraphCategory {
        private TipoServico categoria;
        private Long pedidosPagos;
        private Long pedidosLancados;
        private Long pedidosAReceberPeriodo;
        private Long pedidosRecebidosPeriodo;
        private Long gastos;
    }
}

package Development.Rodrigues.Almeidas_Cortes.finance.entities;

import java.util.List;
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphData {
        private String label;
        private List<Double> data;
    }
}

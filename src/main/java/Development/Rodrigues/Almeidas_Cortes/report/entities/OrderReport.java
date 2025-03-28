package Development.Rodrigues.Almeidas_Cortes.report.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderReport {
    private Long id;
    
    private String nomeModelo;
    
    private String color;
    
    private Long qtdPecas;
    
    private String nomeCliente;

    private String totalDinheiro;

    private String dataPedido;

    private String diaSemana;

    private List<Map<String, String>> grade;

    private Long totalPares;

    private String quemAssinou;

    private String dataRetirada;

    private String horaRetirada;
    
    public OrderReport(
        Long id, 
        Model modelo, 
        String cor, 
        Long totalPares, 
        Client cliente, 
        Double totalDinheiro, 
        String dataPedido, 
        String diaSemana, 
        String grade, 
        Long qtdPecas, 
        String quemAssinou, 
        String dataRetirada, 
        String horaRetirada
    ) {
        this.id = id;
        this.nomeModelo = modelo.getTipo();
        this.color = cor;
        this.qtdPecas = qtdPecas;
        this.nomeCliente = cliente.getNome();
        this.totalDinheiro = String.format("%.2f", totalDinheiro);;
        this.dataPedido = dataPedido;
        this.diaSemana = diaSemana;
        this.grade = parseGrades(grade);
        this.totalPares = totalPares;
        this.quemAssinou = quemAssinou;
        this.dataRetirada = dataRetirada;
        this.horaRetirada = horaRetirada;
    }

    private List<Map<String, String>> parseGrades(String gradeString) {
        String[] gradePairs = gradeString.split(",");
        return List.of(gradePairs).stream()
                .map(pair -> {
                    String[] keyValue = pair.split(":");  // Divide cada par por ":"
                    Map<String, String> map = new HashMap<>();
                    if (keyValue.length == 2) {
                        map.put(keyValue[0], keyValue[1]);  // Adiciona o par chave-valor no mapa
                    }
                    return map;
                })
                .collect(Collectors.toList());
    }
}

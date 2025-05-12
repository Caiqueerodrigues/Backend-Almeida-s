package Development.Rodrigues.Almeidas_Cortes.report.entities;

import java.util.ArrayList;
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

    private Long qtdFacas;
    
    private String nomeCliente;

    private String totalDinheiro;

    private String dataPedido;

    private String diaSemana;

    private List<Map<String, String>> grade;

    private Long totalPares;

    private String quemAssinou;

    private String dataRetirada;

    private String horaRetirada;

    private String precoPar;

    private String dataPagamento;

    private String obs;
    
    private Long quantidadeVias;

    private String material;
    
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
        String horaRetirada,
        Double precoPar,
        String dataPagamento,
        String obs,
        Long quantidadeVias,
        Long qtdFacas,
        String material
    ) {
        this.id = id;
        this.nomeModelo = modelo.getTipo().toUpperCase();
        this.color = cor;
        this.qtdPecas = qtdPecas;
        this.nomeCliente = cliente.getNome();
        this.totalDinheiro = String.format("%.2f", totalDinheiro);
        this.dataPedido = dataPedido;
        this.diaSemana = diaSemana;
        this.grade = parseGrades(grade);
        this.totalPares = totalPares;
        this.quemAssinou = quemAssinou;
        this.dataRetirada = dataRetirada;
        this.horaRetirada = horaRetirada;
        this.precoPar = (String.valueOf(precoPar).matches(".*\\.\\d{3}$")) ? String.format("%.3f", precoPar) : String.format("%.2f", precoPar);
        this.dataPagamento = dataPagamento;
        this.obs = obs;
        this.quantidadeVias = quantidadeVias;
        this.qtdFacas = qtdFacas;
        this.material = material;
    }

    private List<Map<String, String>> parseGrades(String gradeString) {
        List<Map<String, String>> result = new ArrayList<>();
        gradeString = gradeString.replaceAll("\\s+", "");

        String[] parts = gradeString.split(",");
        for (String part : parts) {
            // Dividir por ":" para obter chave e valor
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                Map<String, String> entry = new HashMap<>();
                entry.put("numeracao", keyValue[0]);
                entry.put("valor", keyValue[1]);
                result.add(entry);
            } else if (keyValue.length == 1) {
                Map<String, String> entry = new HashMap<>();
                entry.put("numeracao", keyValue[0]);
                entry.put("valor", "");
                result.add(entry);
            }
        }

        return result;
    }
}

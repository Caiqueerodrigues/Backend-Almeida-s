package Development.Rodrigues.Almeidas_Cortes.historyOrders.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "List_History")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ListHistoryOrders {
    @Id
    private Long id;

    private Long idPedido;

    private String nomeCliente;

    private double totalDinheiro;

    private String modelo;

    private List<String> cor;

    private double precoPar;

    private String horaModificacao;
    
    private String nomeUsuario;

    private String operacaoRealizada;

    public ListHistoryOrders (
        Long id,
        Long idPedido,
        String nomeCliente,
        double total,
        String modelo,
        List<String> cor,
        double preco,
        LocalDateTime data,
        String nome,
        String operacaoRealizada
    ) {
        this.id = id;
        this.idPedido = idPedido;
        this.nomeCliente = nomeCliente;
        this.totalDinheiro = total;
        this.modelo = modelo;
        this.cor = cor;
        this.precoPar = preco;
        this.horaModificacao = data.toString().split("T")[1];
        this.nomeUsuario = nome;
        this.operacaoRealizada = operacaoRealizada;
    }
}

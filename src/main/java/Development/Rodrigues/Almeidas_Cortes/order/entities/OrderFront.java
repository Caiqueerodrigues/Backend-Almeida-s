package Development.Rodrigues.Almeidas_Cortes.order.entities;

import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "OrderFront")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderFront {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_Client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "modelo", nullable = false)
    private Model modelo;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;
    
    @Column(name = "data_finalizado", nullable = false)
    private LocalDateTime dataFinalizado;
    
    @Column(name = "relatorio_Cliente", length = 100)
    private String relatorioCliente;
    
    @Column(name = "total_Dinheiro", nullable = false)
    private Double totalDinheiro;
    
    @Column(name = "total_Pares", nullable = false)
    private Long totalPares;
    
    @Column(name = "grade", nullable = false, columnDefinition = "TEXT")
    private String grade;
    
    @Column(name = "obs", length = 255)
    private String obs;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;
    
    @Column(name = "tipo_Recebido", columnDefinition = "TEXT")
    private String tipoRecebido;
    
    @Column(name = "rendimento_Pares_Metro")
    private String rendimentoParesMetro;
    
    @Column(name = "quem_Assinou", length = 100)
    private String quemAssinou;

    @Column(name = "cor", nullable = false)
    private String cor;

    @Column(name = "data_Retirada")
    private LocalDateTime dataRetirada;

    @Column(name = "quem_cortou")
    private String quemCortou;
    
    public OrderFront(
        Client client,
        Model modelo,
        LocalDateTime dataPedido,
        LocalDateTime dataFinalizado,
        String relatorioCliente,
        Double totalDinheiro,
        Long totalPares,
        String grade,
        String obs,
        LocalDateTime dataPagamento,
        String tipoRecebido,
        String rendimento,
        String quemAssinou,
        String cor,
        LocalDateTime dataRetirada,
        String quemCortou
    ) {
        this.client = client;
        this.modelo = modelo;
        this.dataPedido = dataPedido;
        this.dataFinalizado = dataFinalizado;
        this.relatorioCliente = relatorioCliente;
        this.totalDinheiro = totalDinheiro;
        this.totalPares = totalPares;
        this.grade = grade;
        this.obs = obs;
        this.dataPagamento = dataPagamento;
        this.tipoRecebido = tipoRecebido;
        this.rendimentoParesMetro = rendimento;
        this.quemAssinou = quemAssinou;
        this.cor = cor;
        this.dataRetirada = dataRetirada;
        this.quemCortou = quemCortou;
    }
}

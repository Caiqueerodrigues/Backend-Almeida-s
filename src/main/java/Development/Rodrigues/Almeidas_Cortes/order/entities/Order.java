package Development.Rodrigues.Almeidas_Cortes.order.entities;

import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_pedidos")
@Entity(name = "Order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

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
    
    public Order(CreateOrderDTO dados) {
        this.client = dados.client();
        this.modelo = dados.modelo();
        this.dataPedido = dados.dataPedido();
        this.dataFinalizado = dados.dataFinalizado();
        this.relatorioCliente = dados.relatorioCliente();
        this.totalDinheiro = dados.totalDinheiro();
        this.totalPares = dados.totalPares();
        this.grade = dados.grade();
        this.obs = dados.obs();
        this.dataPagamento = dados.dataPagamento();
        this.tipoRecebido = dados.tipoRecebido();
        this.rendimentoParesMetro = dados.rendimento();
        this.quemAssinou = dados.quemAssinou();
        this.cor = dados.cor();
        this.dataRetirada = dados.dataRetirada();
        this.quemCortou = dados.quemCortou();
    }

    public void updateOrder(UpdateOrderDTO dados) {
        this.client = dados.client();
        this.modelo = dados.modelo();
        this.dataPedido = dados.dataPedido();
        this.dataFinalizado = dados.dataFinalizado();
        this.relatorioCliente = dados.relatorioCliente();
        this.totalDinheiro = dados.totalDinheiro();
        this.totalPares = dados.totalPares();
        this.grade = dados.grade();
        this.obs = dados.obs();
        this.dataPagamento = dados.dataPagamento();
        this.tipoRecebido = dados.tipoRecebido();
        this.rendimentoParesMetro = dados.rendimento();
        this.quemAssinou = dados.quemAssinou();
        this.cor = dados.cor();
        this.dataRetirada = dados.dataRetirada();
        this.quemCortou = dados.quemCortou();
    }
}

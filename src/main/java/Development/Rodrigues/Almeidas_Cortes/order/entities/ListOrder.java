package Development.Rodrigues.Almeidas_Cortes.order.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.order.dto.ListOrdersDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "List_Pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ListOrder {
    @Id
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

    @Column(name = "total_Pecas", nullable = false)
    private Long totalPecas;

    @Column(name = "grade", nullable = false)
    private Object grade;

    @Column(name = "obs", length = 255)
    private String obs;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "metragem_Recebido")
    private List<String> metragemRecebido;

    @Column(name = "tipo_Recebido")
    private List<Object> tipoRecebido;

    @Column(name = "metragem_Finalizado")
    private List<String> metragemFinalizado;

    @Column(name = "rendimento_Pares_Metro")
    private List<String> rendimentoParesMetro;

    public ListOrder(ListOrdersDTO dados) {
        this.id = dados.id();
        this.client = dados.client();
        this.modelo = dados.modelo();
        this.dataPedido = dados.dataPedido();
        this.dataFinalizado = dados.dataFinalizado();
        this.relatorioCliente = dados.relatorioCliente();
        this.totalDinheiro = dados.totalDinheiro();
        this.totalPares = dados.totalPares();
        this.totalPecas = dados.totalPecas();
        this.grade = dados.grade();
        this.obs = dados.obs();
        this.dataPagamento = dados.dataPagamento();
        this.metragemRecebido = dados.metragemRecebido();
        this.tipoRecebido = dados.tipoRecebido();
        this.metragemFinalizado = dados.metragemFinalizado();
        this.rendimentoParesMetro = dados.rendimentoParesMetro();
    }
}

package Development.Rodrigues.Almeidas_Cortes.order.entities;

import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_pedidos_backup")
@Entity(name = "OrderBackup")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderBackup {

    @Id
    private Long id;

    @Column(name = "id_Client", nullable = false)
    private Long client;

    @Column(name = "modelo", nullable = false)
    private Long modelo;

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

    @Column(name = "id_User", nullable = false)
    private Long user;
    
    public OrderBackup(Order dados) {
        this.id = dados.getId();
        this.client = dados.getClient().getId();
        this.modelo = dados.getModelo().getId();
        this.dataPedido = dados.getDataPedido();
        this.dataFinalizado = dados.getDataFinalizado();
        this.relatorioCliente = dados.getRelatorioCliente();
        this.totalDinheiro = dados.getTotalDinheiro();
        this.totalPares = dados.getTotalPares();
        this.grade = dados.getGrade();
        this.obs = dados.getObs();
        this.dataPagamento = dados.getDataPagamento();
        this.tipoRecebido = dados.getTipoRecebido();
        this.rendimentoParesMetro = dados.getRendimentoParesMetro();
        this.quemAssinou = dados.getQuemAssinou();
        this.cor = dados.getCor();
        this.dataRetirada = dados.getDataRetirada();
        this.quemCortou = dados.getQuemCortou();
        this.user = dados.getUser().getId();
    }
}

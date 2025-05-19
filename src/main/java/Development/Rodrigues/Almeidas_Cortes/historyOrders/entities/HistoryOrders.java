package Development.Rodrigues.Almeidas_Cortes.historyOrders.entities;

import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tab_history_orders")
@Entity(name = "history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class HistoryOrders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_Pedido", nullable = false)
    private Order idPedido;
    
    @Column(name = "operation", nullable = false)
    private String operation;
    
    @Column(name = "update_At", nullable = false)
    private LocalDateTime updateAt;
    
    @ManyToOne
    @JoinColumn(name = "id_User", nullable = false)
    private User idUser;

    public HistoryOrders ( 
        Order pedido,
        String operation,
        LocalDateTime date,
        User user
    ) {
        this.idPedido = pedido;
        this.operation = operation;
        this.updateAt = date;
        this.idUser = user;
    }
}

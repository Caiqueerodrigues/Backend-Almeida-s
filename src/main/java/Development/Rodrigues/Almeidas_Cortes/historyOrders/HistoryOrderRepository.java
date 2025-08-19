package Development.Rodrigues.Almeidas_Cortes.historyOrders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.historyOrders.entities.HistoryOrders;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;

public interface HistoryOrderRepository extends JpaRepository<HistoryOrders, Long> {
    List<HistoryOrders> findByUpdateAtBetweenOrderByIdDesc(LocalDateTime start, LocalDateTime end);

    List<HistoryOrders> findByIdPedido(Order id);
}

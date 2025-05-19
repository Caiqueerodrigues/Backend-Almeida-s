package Development.Rodrigues.Almeidas_Cortes.historyOrders;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.historyOrders.entities.HistoryOrders;

public interface HistoryOrderRepository extends JpaRepository<HistoryOrders, Long> {
    List<HistoryOrders> findByUpdateAtBetween(LocalDateTime start, LocalDateTime end);
}

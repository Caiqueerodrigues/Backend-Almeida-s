package Development.Rodrigues.Almeidas_Cortes.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findByDataPedidoBetween(LocalDateTime startDate, LocalDateTime endDate);
};

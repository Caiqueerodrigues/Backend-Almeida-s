package Development.Rodrigues.Almeidas_Cortes.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findByDataPedido(LocalDate dataPedido);
}

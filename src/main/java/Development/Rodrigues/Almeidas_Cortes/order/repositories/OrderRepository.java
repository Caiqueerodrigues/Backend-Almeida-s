package Development.Rodrigues.Almeidas_Cortes.order.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findByDataPedidoBetweenOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPagamentoBetweenOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPedidoBetweenAndCategoriaOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    
    @Query("SELECT o FROM Order o " +
        "WHERE o.dataPedido BETWEEN :startDate AND :endDate " +
        "AND o.client.id = :clientId " +
        "AND ((:includePaid = true AND o.dataPagamento IS NOT NULL) OR (:includePaid = false AND o.dataPagamento IS NULL))")
    List<Order> findOrdersByRangeByClientPaidOrNot(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("clientId") Long clientId,
        @Param("includePaid") boolean includePaid
    );

    List<Order> findByClientIdAndDataPagamentoIsNull(Long id);
    List<Order> findByClientIdAndDataPagamentoIsNotNull(Long id);

    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNull(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNotNull(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNullAndCategoria(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNotNullAndCategoria(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    List<Order> findByDataPedidoBetweenAndClientId(LocalDateTime startDate, LocalDateTime endDate, Long clientId);

    List<Order> findByClientIdAndDataRetiradaIsNull(Long clientId);
    List<Order> findByDataRetiradaIsNull();

    List<Order> findByClientId(Long clientId);

    List<Order> findByIdIn(List<Long> ids);

    List<Order> findByDataPagamentoIsNotNull();
    List<Order> findByDataPagamentoIsNull();

    List<Order> findAll();

};

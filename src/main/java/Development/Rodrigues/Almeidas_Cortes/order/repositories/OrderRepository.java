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

    List<Order> findByDataPedidoBetweenAndExcluidoIsFalseOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPagamentoBetweenAndExcluidoIsFalseOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPedidoBetweenAndCategoriaAndExcluidoIsFalseOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    
    @Query("SELECT o FROM Order o " +
        "WHERE o.dataPedido BETWEEN :startDate AND :endDate " +
        "AND o.client.id = :clientId " +
        "AND ((:includePaid = true AND o.dataPagamento IS NOT NULL) OR (:includePaid = false AND o.dataPagamento IS NULL))" + 
        "AND o.excluido IS FALSE"
    )
    List<Order> findOrdersByRangeByClientPaidOrNot(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("clientId") Long clientId,
        @Param("includePaid") boolean includePaid
    );

    List<Order> findByClientIdAndDataPagamentoIsNullAndExcluidoIsFalse(Long id);
    List<Order> findByClientIdAndDataPagamentoIsNotNullAndExcluidoIsFalse(Long id);

    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNullAndExcluidoIsFalse(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNotNullAndExcluidoIsFalse(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNullAndCategoriaAndExcluidoIsFalse(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    List<Order> findByDataPedidoBetweenAndDataPagamentoIsNotNullAndCategoriaAndExcluidoIsFalse(LocalDateTime startDate, LocalDateTime endDate, String categoria);
    List<Order> findByDataPedidoBetweenAndClientIdAndExcluidoIsFalse(LocalDateTime startDate, LocalDateTime endDate, Long clientId);

    List<Order> findByClientIdAndDataRetiradaIsNullAndExcluidoIsFalse(Long clientId);
    List<Order> findByDataRetiradaIsNullAndExcluidoIsFalse();

    List<Order> findByClientIdAndExcluidoIsFalse(Long clientId);

    List<Order> findByIdIn(List<Long> ids);

    List<Order> findByDataPagamentoIsNotNullAndExcluidoIsFalse();
    List<Order> findByDataPagamentoIsNullAndExcluidoIsFalse();

    List<Order> findAll();

};

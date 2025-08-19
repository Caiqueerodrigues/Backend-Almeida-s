package Development.Rodrigues.Almeidas_Cortes.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.order.entities.OrderBackup;


public interface OrderRepositoryBackup extends JpaRepository<OrderBackup, Long> {
    
};

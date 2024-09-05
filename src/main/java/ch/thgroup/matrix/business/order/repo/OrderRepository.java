package ch.thgroup.matrix.business.order.repo;

import ch.thgroup.matrix.business.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAll();

    Optional<OrderEntity> findByOrderIdAndActiveTrue(Long orderId);

    List<OrderEntity> findByActiveTrueOrderByOrderIdDesc();
}
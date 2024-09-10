package ch.thgroup.matrix.business.order.repo;

import ch.thgroup.matrix.business.order.entity.OrderItemEntity;
import ch.thgroup.matrix.business.order.entity.OrderEntity;
import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrder(OrderEntity order);

    void deleteByOrderAndArticle(OrderEntity order, ArticleEntity article);

    Optional<OrderItemEntity> findByOrderItemId(Long orderItemId);

    List<OrderItemEntity> findByOrderItemIdIn(List<Long> orderItemIds);

}
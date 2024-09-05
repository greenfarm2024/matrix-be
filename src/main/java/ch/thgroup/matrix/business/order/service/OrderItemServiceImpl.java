package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import ch.thgroup.matrix.business.admin.repo.UserRepository;
import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import ch.thgroup.matrix.business.article.repository.ArticleRepository;
import ch.thgroup.matrix.business.order.dto.OrderItemDTO;
import ch.thgroup.matrix.business.order.dto.OrderItemMapper;
import ch.thgroup.matrix.business.order.entity.OrderEntity;
import ch.thgroup.matrix.business.order.entity.OrderItemEntity;
import ch.thgroup.matrix.business.order.repo.OrderItemRepository;
import ch.thgroup.matrix.business.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public List<OrderItemDTO> crudOrderItemList(List<OrderItemDTO> orderItemListDTO) {
        try {
            var order = orderRepository.findById(orderItemListDTO.get(0).getOrderId()).orElseThrow(() -> {
                log.error("Order not found with id: {}", orderItemListDTO.get(0).getOrderId());
                return new IllegalArgumentException("Order not found");
            });

            var client = userRepository.findById(orderItemListDTO.get(0).getClientId()).orElseThrow(() -> {
                log.error("Client not found with id: {}", orderItemListDTO.get(0).getClientId());
                return new IllegalArgumentException("Client not found");
            });

            List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder(order);

            for (OrderItemDTO orderItemDTO : orderItemListDTO) {
                var article = articleRepository.findByArticleIdAndActiveTrue(orderItemDTO.getArticleId()).orElseThrow(() -> {
                    log.error("Article not found for ID: {}", orderItemDTO.getArticleId());
                    return new IllegalArgumentException("Article not found for ID: " + orderItemDTO.getArticleId());
                });

                var existingOrderItem = orderItemEntities.stream()
                        .filter(orderItemEntity -> orderItemEntity.getArticle().getArticleId().equals(orderItemDTO.getArticleId()))
                        .findFirst()
                        .orElse(null);

                if (existingOrderItem != null) {
                    handleExistingOrderItem(orderItemDTO, existingOrderItem, order, article, client);
                } else if (orderItemDTO.getDelivUnit() != null && orderItemDTO.getDelivUnit() != 0) {
                    createNewOrderItem(orderItemDTO, order, article, client);
                }
            }

            return OrderItemMapper.toDTOs(orderItemRepository.findByOrder(order));
        } catch (IllegalArgumentException e) {
            log.error("Error processing order items: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing order items: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing order items", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order not found with id: {}", orderId);
            return new IllegalArgumentException("Order not found");
        });

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder(order);
        return OrderItemMapper.toDTOs(orderItemEntities);
    }

    private void handleExistingOrderItem(OrderItemDTO orderItemDTO, OrderItemEntity existingOrderItem, OrderEntity order, ArticleEntity article, UserEntity client) {
        if (orderItemDTO.getDelivUnit() == null || orderItemDTO.getDelivUnit() == 0) {
            orderItemRepository.deleteByOrderAndArticle(order, article);
            log.info("Order item deleted successfully with id: {}", existingOrderItem.getOrderItemId());
        } else if (!orderItemDTO.getDelivUnit().equals(existingOrderItem.getDelivUnit())) {
            updateOrderItem(existingOrderItem, orderItemDTO);
            log.info("Order item updated successfully with id: {}", existingOrderItem.getOrderItemId());
        }
    }

    private void createNewOrderItem(OrderItemDTO orderItemDTO, OrderEntity order, ArticleEntity article, UserEntity client) {
        OrderItemEntity newOrderItemEntity = new OrderItemEntity();
        newOrderItemEntity.setArticle(article);
        newOrderItemEntity.setOrder(order);
        newOrderItemEntity.setRevision(order.getRevision());
        newOrderItemEntity.setClient(client);
        newOrderItemEntity.setSkuInt(article.getSkuInt());
        newOrderItemEntity.setSkuExt(article.getSkuExt());
        newOrderItemEntity.setArtNameDe(article.getArtNameDe());
        newOrderItemEntity.setArtNameEn(article.getArtNameEn());
        newOrderItemEntity.setArtNameTh(article.getArtNameTh());
        newOrderItemEntity.setDelivUnit(orderItemDTO.getDelivUnit());
        newOrderItemEntity.setUndelSupp1(article.getUndelSupp1());
        newOrderItemEntity.setUndelSupp2(article.getUndelSupp2());
        newOrderItemEntity.setUndelSupp3(article.getUndelSupp3());
        newOrderItemEntity.setSaleUnit(article.getSaleUnit());
        newOrderItemEntity.setSalePrice(article.getSalePrice());
        newOrderItemEntity.setTva(article.getTva());
        newOrderItemEntity.setCreatedBy(orderItemDTO.getClientId().shortValue());

        orderItemRepository.save(newOrderItemEntity);
        log.info("Order item created successfully with id: {}", newOrderItemEntity.getOrderItemId());
    }

    private void updateOrderItem(OrderItemEntity existingOrderItem, OrderItemDTO orderItemDTO) {
        existingOrderItem.setDelivUnit(orderItemDTO.getDelivUnit());
        existingOrderItem.setUpdatedBy(orderItemDTO.getClientId().shortValue());
        orderItemRepository.save(existingOrderItem);
    }
}
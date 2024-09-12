package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import ch.thgroup.matrix.business.admin.repo.UserRepository;
import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import ch.thgroup.matrix.business.article.repository.ArticleRepository;
import ch.thgroup.matrix.business.common.OrderStatus;
import ch.thgroup.matrix.business.mail.Language;
import ch.thgroup.matrix.business.mail.MailType;
import ch.thgroup.matrix.business.mail.SendMailService;
import ch.thgroup.matrix.business.order.dto.OrderItemDTO;
import ch.thgroup.matrix.business.order.dto.OrderItemMapper;
import ch.thgroup.matrix.business.order.entity.OrderEntity;
import ch.thgroup.matrix.business.order.entity.OrderItemEntity;
import ch.thgroup.matrix.business.order.repo.OrderItemRepository;
import ch.thgroup.matrix.business.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OrderRepository orderRepository;
    private final SendMailService sendMailService;

    @Override
    @Transactional
    public List<OrderItemDTO> crudOrderItemList(@NonNull List<OrderItemDTO> orderItemListDTO) {
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
    public List<OrderItemDTO> getOrderItemsByOrderId(@NonNull Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order not found with id: {}", orderId);
            return new IllegalArgumentException("Order not found");
        });

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder(order);
        return OrderItemMapper.toDTOs(orderItemEntities);
    }

    @Override
    @Transactional
    public List<OrderItemDTO> distributionSplit(@NonNull List<OrderItemDTO> orderItemListDTO) {
        try {
            var getOrderItem = orderItemRepository.findByOrderItemId(orderItemListDTO.getFirst().getOrderItemId()).orElseThrow(() -> {
                log.error("Order item not found with id: {}", orderItemListDTO.getFirst().getOrderItemId());
                return new IllegalArgumentException("Order not found");
            });

            List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder(getOrderItem.getOrder());

            for (OrderItemDTO orderItemDTO : orderItemListDTO) {
                var existingOrderItem = orderItemEntities.stream()
                        .filter(orderItemEntity -> orderItemEntity.getOrderItemId().equals(orderItemDTO.getOrderItemId()))
                        .findFirst()
                        .orElse(null);

                if (existingOrderItem != null) {
                    handleDistributionSplit(orderItemDTO, existingOrderItem);
                } else {
                    log.error("Order item not found with id: {}", orderItemDTO.getOrderItemId());
                    throw new RuntimeException("Order item not found with id: " + orderItemDTO.getOrderItemId());
                }
            }

            return OrderItemMapper.toDTOs(orderItemRepository.findByOrder(getOrderItem.getOrder()));
        } catch (IllegalArgumentException e) {
            log.error("Error processing distribution split: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing distribution split: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing distribution split", e);
        }
    }

    @Override
    @Transactional
    public List<OrderItemDTO> confirmationSupplier(@NonNull List<OrderItemDTO> orderItemListDTO) {
        try {
            OrderEntity order = null;

            List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderItemIdIn(orderItemListDTO.stream()
                    .map(OrderItemDTO::getOrderItemId)
                    .toList());

            for (OrderItemDTO orderItemDTO : orderItemListDTO) {
                var existingOrderItem = orderItemEntities.stream()
                        .filter(orderItemEntity -> orderItemEntity.getOrderItemId().equals(orderItemDTO.getOrderItemId()))
                        .findFirst()
                        .orElse(null);

                if (existingOrderItem != null) {
                    order = existingOrderItem.getOrder();
                    handleConfirmationSupplier(orderItemDTO, existingOrderItem);
                } else {
                    log.error("Order item for confirmation supplier not found with id: {}", orderItemDTO.getOrderItemId());
                    throw new RuntimeException("Order item for confirmation supplier not found with id: " + orderItemDTO.getOrderItemId());
                }
            }

            return OrderItemMapper.toDTOs(orderItemRepository.findByOrder(order));
        } catch (IllegalArgumentException e) {
            log.error("Error processing distribution split: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing distribution split: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing distribution split", e);
        }
    }

    @Override
    @Transactional
    public List<OrderItemDTO> validateDeliveryQuantity(@NonNull List<OrderItemDTO> orderItemListDTO) {
        try {
            OrderEntity order = null;

            List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderItemIdIn(orderItemListDTO.stream()
                    .map(OrderItemDTO::getOrderItemId)
                    .toList());

            for (OrderItemDTO orderItemDTO : orderItemListDTO) {
                var existingOrderItem = orderItemEntities.stream()
                        .filter(orderItemEntity -> orderItemEntity.getOrderItemId().equals(orderItemDTO.getOrderItemId()))
                        .findFirst()
                        .orElse(null);

                if (existingOrderItem != null) {
                    order = existingOrderItem.getOrder();
                    handleValidateDeliveryQuantity(orderItemDTO, existingOrderItem, existingOrderItem.getArticle());
                } else {
                    log.error("Order item for confirmation supplier not found with id: {}", orderItemDTO.getOrderItemId());
                    throw new RuntimeException("Order item for confirmation supplier not found with id: " + orderItemDTO.getOrderItemId());
                }
            }

            return OrderItemMapper.toDTOs(orderItemRepository.findByOrder(order));
        } catch (IllegalArgumentException e) {
            log.error("Error processing distribution split: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing distribution split: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing distribution split", e);
        }
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

    private void handleDistributionSplit(OrderItemDTO orderItemDTO, OrderItemEntity existingOrderItem) {
        if (!orderItemDTO.getDelivSupp1().equals(existingOrderItem.getDelivSupp1())
                || !orderItemDTO.getDelivSupp2().equals(existingOrderItem.getDelivSupp2())
                || !orderItemDTO.getDelivSupp3().equals(existingOrderItem.getDelivSupp3())) {
            updateDistributionSplit(existingOrderItem, orderItemDTO);
            log.info("Distribution split successfully with id: {}", existingOrderItem.getOrderItemId());
        }
    }

    private void updateDistributionSplit(OrderItemEntity existingOrderItem, OrderItemDTO orderItemDTO) {
        existingOrderItem.setDelivSupp1(orderItemDTO.getDelivSupp1());
        existingOrderItem.setDelivSupp2(orderItemDTO.getDelivSupp2());
        existingOrderItem.setDelivSupp3(orderItemDTO.getDelivSupp3());
        existingOrderItem.setUpdatedBy(orderItemDTO.getUpdatedBy());
        orderItemRepository.save(existingOrderItem);
    }

    private void handleConfirmationSupplier(OrderItemDTO orderItemDTO, OrderItemEntity existingOrderItem) {
        if (orderItemDTO.getConfSupp1() != null &&  !orderItemDTO.getConfSupp1().equals(existingOrderItem.getConfSupp1())) {
            log.info("set confirmation supplier 1 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setConfSupp1(orderItemDTO.getConfSupp1());
        } else if (orderItemDTO.getConfSupp2() != null && !orderItemDTO.getConfSupp2().equals(existingOrderItem.getConfSupp2())) {
            log.info("set confirmation supplier 2 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setConfSupp2(orderItemDTO.getConfSupp2());
        } else if (orderItemDTO.getConfSupp3() != null && !orderItemDTO.getConfSupp3().equals(existingOrderItem.getConfSupp3())) {
            log.info("set confirmation supplier 3 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setConfSupp3(orderItemDTO.getConfSupp3());
        }
        existingOrderItem.setUpdatedBy(orderItemDTO.getUpdatedBy());
        orderItemRepository.save(existingOrderItem);
        log.info("confirmation supplier successfully with id: {}", existingOrderItem.getOrderItemId());
    }

    private void handleValidateDeliveryQuantity(OrderItemDTO orderItemDTO, OrderItemEntity existingOrderItem, ArticleEntity article) {
        if (orderItemDTO.getRealSupp1() != null &&  !orderItemDTO.getRealSupp1().equals(existingOrderItem.getRealSupp1())) {
            log.info("set real delivery for supplier 1 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setRealSupp1(orderItemDTO.getRealSupp1());
            var  undelSupp1 = (short) (existingOrderItem.getRealSupp1() - existingOrderItem.getConfSupp1());
            existingOrderItem.setUndelSupp1(undelSupp1);
            article.setUndelSupp1(undelSupp1);
        } else if (orderItemDTO.getRealSupp2() != null && !orderItemDTO.getRealSupp2().equals(existingOrderItem.getRealSupp2())) {
            log.info("set real delivery for supplier 2 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setRealSupp2(orderItemDTO.getRealSupp2());
            var  undelSupp2 = (short) (existingOrderItem.getRealSupp2() - existingOrderItem.getConfSupp2());
            existingOrderItem.setUndelSupp2(undelSupp2);
            article.setUndelSupp1(undelSupp2);
        } else if (orderItemDTO.getRealSupp3() != null && !orderItemDTO.getRealSupp3().equals(existingOrderItem.getRealSupp3())) {
            log.info("set real delivery for supplier 3 for order item id: {}", existingOrderItem.getOrderItemId());
            existingOrderItem.setRealSupp3(orderItemDTO.getRealSupp3());
            var  undelSupp3 = (short) (existingOrderItem.getRealSupp3() - existingOrderItem.getConfSupp3());
            existingOrderItem.setUndelSupp3(undelSupp3);
            article.setUndelSupp3(undelSupp3);
        }
        existingOrderItem.setUpdatedBy(orderItemDTO.getUpdatedBy());
        orderItemRepository.save(existingOrderItem);
        articleRepository.save(article);
        log.info("real delivery supplier successfully with id: {}", existingOrderItem.getOrderItemId());
    }

    @Override
    @Transactional
    public void publishOrder(Long orderId) {
        var orderEntity = orderRepository.findByOrderIdAndActiveTrue(orderId).orElseThrow(() -> {
            log.error("Active order not found with id: {}", orderId);
            return new IllegalArgumentException("Active order not found");
        });

        if (orderEntity.getOrderItems().isEmpty()) {
            log.error("Order has no items to publish");
            throw new IllegalArgumentException("Order has no items to publish");
        }

        orderEntity.setOrderStatus(OrderStatus.PUBLISHED);
        orderRepository.save(orderEntity);

        StringBuilder orderItemsTable = new StringBuilder("<table style='border-collapse: collapse; width: 100%;'>");
        orderItemsTable.append("<tr><th style='border: 1px solid black; padding: 8px;'>Artikelname</th><th style='border: 1px solid black; padding: 8px;'>Liefermenge</th><th style='border: 1px solid black; padding: 8px;'>Verkaufseinheit</th></tr>");
        for (OrderItemEntity orderItem : orderEntity.getOrderItems()) {
            orderItemsTable.append("<tr>")
                    .append("<td style='border: 1px solid black; padding: 8px;'>").append(orderItem.getArtNameDe()).append("</td>")
                    .append("<td style='border: 1px solid black; padding: 8px;'>").append(orderItem.getDelivUnit()).append("</td>")
                    .append("<td style='border: 1px solid black; padding: 8px;'>").append(orderItem.getSaleUnit()).append("</td>")
                    .append("</tr>");
        }
        orderItemsTable.append("</table>");

        sendMailService.sendMail(MailType.PUBLISH_ORDER,
                Map.of("orderid", orderEntity.getOrderId().toString(),
                        "revision", orderEntity.getRevision().toString(),
                        "client", orderEntity.getClient().getFirstName(),
                        "orderitemslist", orderItemsTable.toString(),
                        "title", orderEntity.getOrderTitle(),
                        "deliverydate", orderEntity.getDeliveryDate().toString()
                ),
                "cristian.voinicaru@thgroup.ch",
                Language.DE);
    }
}
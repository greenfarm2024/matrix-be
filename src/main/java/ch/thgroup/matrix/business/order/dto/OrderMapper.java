package ch.thgroup.matrix.business.order.dto;

import ch.thgroup.matrix.business.common.OrderStatus;
import ch.thgroup.matrix.business.order.entity.OrderEntity;

public class OrderMapper {

    public static OrderDTO toDTO(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }

        return OrderDTO.builder()
                .orderId(orderEntity.getOrderId())
                .orderReference(orderEntity.getOrderReference())
                .revision(orderEntity.getRevision())
                .clientId(orderEntity.getClient().getUserId())
                .orderTitle(orderEntity.getOrderTitle())
                .deliveryDate(orderEntity.getDeliveryDate())
                .orderStatus(OrderStatus.valueOf(orderEntity.getOrderStatus().toString()))
                .active(orderEntity.isActive())
                .createdDate(orderEntity.getCreatedDate())
                .createdBy(orderEntity.getCreatedBy())
                .lastUpdatedDate(orderEntity.getLastUpdatedDate())
                .updatedBy(orderEntity.getUpdatedBy())
                .build();
    }

    ///change the method toEntity to not set the values if the field from dto is null

    public static OrderEntity toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderReference(orderDTO.getOrderReference());
        orderEntity.setRevision(orderDTO.getRevision());
        orderEntity.setOrderTitle(orderDTO.getOrderTitle());
        orderEntity.setDeliveryDate(orderDTO.getDeliveryDate());
        orderEntity.setOrderStatus(OrderStatus.valueOf(orderDTO.getOrderStatus().toString()));
        orderEntity.setCreatedBy(orderDTO.getCreatedBy());
        orderEntity.setUpdatedBy(orderDTO.getUpdatedBy());
        return orderEntity;
    }
}

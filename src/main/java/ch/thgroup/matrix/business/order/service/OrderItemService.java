package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.order.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> crudOrderItemList(List<OrderItemDTO> orderItemListDTO);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);
}
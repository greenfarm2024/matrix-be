package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.order.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> crudOrderItemList(List<OrderItemDTO> orderItemListDTO);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);

    List<OrderItemDTO> distributionSplit(List<OrderItemDTO> orderItemListDTO);

    List<OrderItemDTO> confirmationSupplier(List<OrderItemDTO> orderItemListDTO);

    List<OrderItemDTO> validateDeliveryQuantity(List<OrderItemDTO> orderItemListDTO);

    void publishOrder(Long orderId);

    List<OrderItemDTO> crudOrderChrItemList(List<OrderItemDTO> orderItemListDTO);

    void approveOrderChr(Long orderId);

    void rejectOrderChr(Long orderId);
}
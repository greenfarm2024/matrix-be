package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(OrderDTO orderDTO);

    void deleteOrder(Long orderId);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();
}

package ch.thgroup.matrix.business.order.service;

import ch.thgroup.matrix.business.common.OrderStatus;
import ch.thgroup.matrix.business.common.exception.NotFoundException;
import ch.thgroup.matrix.business.order.dto.OrderDTO;
import ch.thgroup.matrix.business.order.dto.OrderMapper;
import ch.thgroup.matrix.business.order.entity.OrderEntity;
import ch.thgroup.matrix.business.order.repo.OrderRepository;
import ch.thgroup.matrix.business.admin.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            log.error("OrderDTO is null");
            throw new IllegalArgumentException("OrderDTO cannot be null");
        }

        var client = userRepository.findById(orderDTO.getClientId()).orElseThrow(() -> {
            log.error("Client not found with id: {}", orderDTO.getClientId());
            return new IllegalArgumentException("Client not found");
        });

        orderDTO.setOrderStatus(OrderStatus.DRAFT);
        orderDTO.setRevision((short) 0);
        orderDTO.setCreatedBy(client.getUserId().shortValue());

        var orderEntity = OrderMapper.toEntity(orderDTO);
        orderEntity.setClient(client);

        try {
            orderRepository.save(orderEntity);
            // TODO: Implement the logic for generating order reference
            String currentYear = String.valueOf(java.time.Year.now().getValue());
            orderEntity.setOrderReference(currentYear + "-" + orderEntity.getOrderId());
            orderRepository.save(orderEntity);
            log.info("Order created successfully with id: {}", orderEntity.getOrderId());
            return OrderMapper.toDTO(orderEntity);
        } catch (Exception e) {
            log.error("Error saving order: {}", e.getMessage());
            throw new RuntimeException("Error saving order", e);
        }
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            log.error("OrderDTO is null");
            throw new IllegalArgumentException("OrderDTO cannot be null");
        }

        Long orderId = orderDTO.getOrderId();
        var orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order not found with id: {}", orderId);
            return new NotFoundException("Order not found" + orderId);
        });

        updateOrderEntityFromDTO(orderDTO, orderEntity);

        try {
            orderRepository.save(orderEntity);
            log.info("Order updated successfully with id: {}", orderEntity.getOrderId());
            return OrderMapper.toDTO(orderEntity);
        } catch (Exception e) {
            log.error("Error updating order: {}", e.getMessage());
            throw new RuntimeException("Error updating order", e);
        }
    }

    private void updateOrderEntityFromDTO(OrderDTO orderDTO, OrderEntity orderEntity) {
        orderEntity.setOrderTitle(orderDTO.getOrderTitle());
        orderEntity.setDeliveryDate(orderDTO.getDeliveryDate());
        orderEntity.setUpdatedBy(orderDTO.getUpdatedBy());
    }

    @Override
    public void deleteOrder(Long orderId) {
        var orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order not found with id: {}", orderId);
            return new IllegalArgumentException("Order not found");
        });

        orderEntity.setActive(false);

        try {
            orderRepository.save(orderEntity);
            log.info("Order deleted (deactivated) successfully with id: {}", orderEntity.getOrderId());
        } catch (Exception e) {
            log.error("Error deleting (deactivating) order: {}", e.getMessage());
            throw new RuntimeException("Error deleting (deactivating) order", e);
        }
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        var orderEntity = orderRepository.findByOrderIdAndActiveTrue(orderId).orElseThrow(() -> {
            log.error("Active order not found with id: {}", orderId);
            return new IllegalArgumentException("Active order not found");
        });

        return OrderMapper.toDTO(orderEntity);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findByActiveTrueOrderByOrderIdDesc().stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
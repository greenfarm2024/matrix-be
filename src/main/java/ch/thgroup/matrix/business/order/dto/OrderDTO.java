package ch.thgroup.matrix.business.order.dto;

import ch.thgroup.matrix.business.common.OrderStatus;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {

    private Long orderId;
    private String orderReference;
    private Short revision;
    private Long clientId;
    private String orderTitle;
    private LocalDate deliveryDate;
    private OrderStatus orderStatus;
    private boolean active;
    private LocalDateTime createdDate;
    private Short createdBy;
    private LocalDateTime lastUpdatedDate;
    private Short updatedBy;
}
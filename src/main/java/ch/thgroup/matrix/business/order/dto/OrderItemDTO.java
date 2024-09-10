package ch.thgroup.matrix.business.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderItemDTO {

    private Long orderItemId;
    private Long orderId;
    private Long articleId;
    private Short revision;
    private Long clientId;
    private String skuInt;
    private String skuExt;
    private String artNameDe;
    private String artNameEn;
    private String artNameTh;
    private Integer delivUnit;
    private Integer delivUnitChr;
    private Integer delivSupp1;
    private Integer confSupp1;
    private Short undelSupp1;
    private Short realSupp1;
    private Integer delivSupp2;
    private Integer confSupp2;
    private Short undelSupp2;
    private Short realSupp2;
    private Integer delivSupp3;
    private Integer confSupp3;
    private Short undelSupp3;
    private Short realSupp3;
    private Short alert;
    private Short saleUnit;
    private BigDecimal salePrice;
    private Short tva;
    private boolean active;
    private LocalDateTime createdDate;
    private Short createdBy;
    private LocalDateTime lastUpdatedDate;
    private Short updatedBy;
}
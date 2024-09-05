package ch.thgroup.matrix.business.article.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ArticleDTO {
    private Long articleId;
    private Long clientId;
    private String skuInt;
    private String skuExt;
    private String artNameDe;
    private String artNameEn;
    private String artNameTh;
    private Short saleUnit;
    private BigDecimal salePrice;
    private Short tva;
    private Short undelSupp1;
    private Short undelSupp2;
    private Short undelSupp3;
    private boolean active;
    private LocalDateTime createdDate;
    private Short createdBy;
    private LocalDateTime lastUpdatedDate;
    private Short updatedBy;
}
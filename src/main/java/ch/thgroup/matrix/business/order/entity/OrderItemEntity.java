package ch.thgroup.matrix.business.order.entity;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import ch.thgroup.matrix.business.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_item")
public class OrderItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;

    @Column(name = "revision", nullable = false)
    private Short revision;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "user_id")
    private UserEntity client;

    @Column(name = "sku_int", length = 15, nullable = false)
    private String skuInt;

    @Column(name = "sku_ext", length = 15)
    private String skuExt;

    @Column(name = "art_name_de", length = 100, nullable = false)
    private String artNameDe;

    @Column(name = "art_name_en", length = 100, nullable = false)
    private String artNameEn;

    @Column(name = "art_name_th", length = 100, nullable = false)
    private String artNameTh;

    @Column(name = "deliv_unit", columnDefinition = "int default 0")
    private Integer delivUnit;

    @Column(name = "deliv_unit_chr", columnDefinition = "int default 0")
    private Integer delivUnitChr;

    @Column(name = "deliv_supp1", columnDefinition = "int default 0")
    private Integer delivSupp1;

    @Column(name = "undel_supp1", columnDefinition = "smallint default 0")
    private Short undelSupp1;

    @Column(name = "real_supp1", columnDefinition = "smallint default 0")
    private Short realSupp1;

    @Column(name = "deliv_supp2", columnDefinition = "int default 0")
    private Integer delivSupp2;

    @Column(name = "undel_supp2", columnDefinition = "smallint default 0")
    private Short undelSupp2;

    @Column(name = "real_supp2", columnDefinition = "smallint default 0")
    private Short realSupp2;

    @Column(name = "deliv_supp3", columnDefinition = "int default 0")
    private Integer delivSupp3;

    @Column(name = "undel_supp3", columnDefinition = "smallint default 0")
    private Short undelSupp3;

    @Column(name = "real_supp3", columnDefinition = "smallint default 0")
    private Short realSupp3;

    @Column(name = "alert", columnDefinition = "smallint default 0")
    private Short alert;

    @Column(name = "sale_unit", columnDefinition = "smallint default 0")
    private Short saleUnit;

    @Column(name = "sale_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal salePrice = BigDecimal.valueOf(0.00);

    @Column(name = "tva", columnDefinition = "smallint default 0")
    private Short tva;
}
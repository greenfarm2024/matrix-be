package ch.thgroup.matrix.business.article.entity;

import ch.thgroup.matrix.business.admin.entity.UserEntity;
import ch.thgroup.matrix.business.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "article", schema = "matrix")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ArticleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "user_id")
    private UserEntity client;

    @Column(name = "sku_int", nullable = false, length = 15)
    private String skuInt;

    @Column(name = "sku_ext", length = 15)
    private String skuExt;

    @Column(name = "art_name_de", nullable = false, length = 100)
    private String artNameDe;

    @Column(name = "art_name_en", nullable = false, length = 100)
    private String artNameEn;

    @Column(name = "art_name_th", nullable = false, length = 100)
    private String artNameTh;

    @Column(name = "sale_unit", columnDefinition = "smallint default 0")
    private Short saleUnit;

    @Column(name = "sale_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal salePrice = BigDecimal.valueOf(0.00);

    @Column(name = "tva", columnDefinition = "smallint default 0")
    private Short tva;

    @Column(name = "undel_supp1", columnDefinition = "smallint default 0")
    private Short undelSupp1;

    @Column(name = "undel_supp2", columnDefinition = "smallint default 0")
    private Short undelSupp2;

    @Column(name = "undel_supp3", columnDefinition = "smallint default 0")
    private Short undelSupp3;
}
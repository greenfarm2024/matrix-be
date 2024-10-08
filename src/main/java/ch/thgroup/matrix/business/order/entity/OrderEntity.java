package ch.thgroup.matrix.business.order.entity;

import ch.thgroup.matrix.business.common.OrderStatus;
import ch.thgroup.matrix.business.common.BaseEntity;
import ch.thgroup.matrix.business.admin.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "orders", schema = "matrix")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;

    @Column(name = "order_reference", nullable = false, length = 20)
    private String orderReference;

    @Column(name = "revision", nullable = false)
    private Short revision;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "user_id")
    @NotNull
    private UserEntity client;

    @Column(name = "order_title", nullable = false, length = 200)
    private String orderTitle;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 20)
    @NotNull
    private OrderStatus orderStatus;

    @Column(name = "change_request", nullable = false)
    private boolean isChangeRequest = true;
}


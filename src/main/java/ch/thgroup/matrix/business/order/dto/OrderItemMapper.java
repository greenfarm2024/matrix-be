package ch.thgroup.matrix.business.order.dto;

import ch.thgroup.matrix.business.order.entity.OrderItemEntity;

import java.util.List;

public class OrderItemMapper {

    public static List<OrderItemDTO> toDTOs(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(OrderItemMapper::toDTO)
                .toList();
    }

    public static OrderItemDTO toDTO(OrderItemEntity orderItemEntity) {
        if (orderItemEntity == null) {
            return null;
        }

        return OrderItemDTO.builder()
                .orderItemId(orderItemEntity.getOrderItemId())
                .orderId(orderItemEntity.getOrder().getOrderId())
                .articleId(orderItemEntity.getArticle().getArticleId())
                .revision(orderItemEntity.getRevision())
                .clientId(orderItemEntity.getClient().getUserId())
                .skuInt(orderItemEntity.getSkuInt())
                .skuExt(orderItemEntity.getSkuExt())
                .artNameDe(orderItemEntity.getArtNameDe())
                .artNameEn(orderItemEntity.getArtNameEn())
                .artNameTh(orderItemEntity.getArtNameTh())
                .delivUnit(orderItemEntity.getDelivUnit())
                .delivUnitChr(orderItemEntity.getDelivUnitChr())
                .delivSupp1(orderItemEntity.getDelivSupp1())
                .confSupp1(orderItemEntity.getConfSupp1())
                .undelSupp1(orderItemEntity.getUndelSupp1())
                .realSupp1(orderItemEntity.getRealSupp1())
                .delivSupp2(orderItemEntity.getDelivSupp2())
                .confSupp2(orderItemEntity.getConfSupp2())
                .undelSupp2(orderItemEntity.getUndelSupp2())
                .realSupp2(orderItemEntity.getRealSupp2())
                .delivSupp3(orderItemEntity.getDelivSupp3())
                .confSupp3(orderItemEntity.getConfSupp3())
                .undelSupp3(orderItemEntity.getUndelSupp3())
                .realSupp3(orderItemEntity.getRealSupp3())
                .alert(orderItemEntity.getAlert())
                .saleUnit(orderItemEntity.getSaleUnit())
                .salePrice(orderItemEntity.getSalePrice())
                .tva(orderItemEntity.getTva())
                .active(orderItemEntity.isActive())
                .createdDate(orderItemEntity.getCreatedDate())
                .createdBy(orderItemEntity.getCreatedBy())
                .lastUpdatedDate(orderItemEntity.getLastUpdatedDate())
                .updatedBy(orderItemEntity.getUpdatedBy())
                .build();
    }

    public static OrderItemEntity toEntity(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null) {
            return null;
        }

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setRevision(orderItemDTO.getRevision());
        orderItemEntity.setSkuInt(orderItemDTO.getSkuInt());
        orderItemEntity.setSkuExt(orderItemDTO.getSkuExt());
        orderItemEntity.setArtNameDe(orderItemDTO.getArtNameDe());
        orderItemEntity.setArtNameEn(orderItemDTO.getArtNameEn());
        orderItemEntity.setArtNameTh(orderItemDTO.getArtNameTh());
        orderItemEntity.setDelivUnit(orderItemDTO.getDelivUnit());
        orderItemEntity.setDelivUnitChr(orderItemDTO.getDelivUnitChr());
        orderItemEntity.setConfSupp1(orderItemDTO.getConfSupp1());
        orderItemEntity.setUndelSupp1(orderItemDTO.getUndelSupp1());
        orderItemEntity.setRealSupp1(orderItemDTO.getRealSupp1());
        orderItemEntity.setConfSupp2(orderItemDTO.getConfSupp2());
        orderItemEntity.setUndelSupp2(orderItemDTO.getUndelSupp2());
        orderItemEntity.setRealSupp2(orderItemDTO.getRealSupp2());
        orderItemEntity.setConfSupp3(orderItemDTO.getConfSupp3());
        orderItemEntity.setUndelSupp3(orderItemDTO.getUndelSupp3());
        orderItemEntity.setRealSupp3(orderItemDTO.getRealSupp3());
        orderItemEntity.setAlert(orderItemDTO.getAlert());
        orderItemEntity.setSaleUnit(orderItemDTO.getSaleUnit());
        orderItemEntity.setSalePrice(orderItemDTO.getSalePrice());
        orderItemEntity.setTva(orderItemDTO.getTva());
        return orderItemEntity;
    }
}
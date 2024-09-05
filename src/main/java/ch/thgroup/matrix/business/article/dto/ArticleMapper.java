package ch.thgroup.matrix.business.article.dto;

import ch.thgroup.matrix.business.article.entity.ArticleEntity;

public class ArticleMapper {

    public static ArticleDTO toDTO(ArticleEntity articleEntity) {
        if (articleEntity == null) {
            return null;
        }

        return ArticleDTO.builder()
                .articleId(articleEntity.getArticleId())
                .clientId(articleEntity.getClient().getUserId())
                .skuInt(articleEntity.getSkuInt())
                .skuExt(articleEntity.getSkuExt())
                .artNameDe(articleEntity.getArtNameDe())
                .artNameEn(articleEntity.getArtNameEn())
                .artNameTh(articleEntity.getArtNameTh())
                .saleUnit(articleEntity.getSaleUnit())
                .salePrice(articleEntity.getSalePrice())
                .tva(articleEntity.getTva())
                .undelSupp1(articleEntity.getUndelSupp1())
                .undelSupp2(articleEntity.getUndelSupp2())
                .undelSupp3(articleEntity.getUndelSupp3())
                .active(articleEntity.isActive())
                .createdDate(articleEntity.getCreatedDate())
                .createdBy(articleEntity.getCreatedBy())
                .lastUpdatedDate(articleEntity.getLastUpdatedDate())
                .updatedBy(articleEntity.getUpdatedBy())
                .build();
    }

    public static ArticleEntity toEntity(ArticleDTO articleDTO) {
        if (articleDTO == null) {
            return null;
        }
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticleId(articleDTO.getArticleId());
        articleEntity.setSkuInt(articleDTO.getSkuInt());
        articleEntity.setSkuExt(articleDTO.getSkuExt());
        articleEntity.setArtNameDe(articleDTO.getArtNameDe());
        articleEntity.setArtNameEn(articleDTO.getArtNameEn());
        articleEntity.setArtNameTh(articleDTO.getArtNameTh());
        articleEntity.setSaleUnit(articleDTO.getSaleUnit());
        articleEntity.setSalePrice(articleDTO.getSalePrice());
        articleEntity.setTva(articleDTO.getTva());
        articleEntity.setUndelSupp1(articleDTO.getUndelSupp1());
        articleEntity.setUndelSupp2(articleDTO.getUndelSupp2());
        articleEntity.setUndelSupp3(articleDTO.getUndelSupp3());
        articleEntity.setCreatedDate(articleDTO.getCreatedDate());
        articleEntity.setCreatedBy(articleDTO.getCreatedBy());
        articleEntity.setLastUpdatedDate(articleDTO.getLastUpdatedDate());
        articleEntity.setUpdatedBy(articleDTO.getUpdatedBy());
        return articleEntity;
    }
}
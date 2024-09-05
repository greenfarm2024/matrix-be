package ch.thgroup.matrix.business.article.service;

import ch.thgroup.matrix.business.admin.repo.UserRepository;
import ch.thgroup.matrix.business.article.dto.ArticleDTO;
import ch.thgroup.matrix.business.article.dto.ArticleMapper;

import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import ch.thgroup.matrix.business.article.repository.ArticleRepository;
import ch.thgroup.matrix.business.common.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        if (articleDTO == null) {
            log.error("ArticleDTO is null");
            throw new IllegalArgumentException("ArticleDTO cannot be null");
        }

        var client = userRepository.findById(articleDTO.getClientId()).orElseThrow(() -> {
            log.error("Client not found with id: {}", articleDTO.getClientId());
            return new IllegalArgumentException("Client not found");
        });

        var articleEntity = ArticleMapper.toEntity(articleDTO);
        articleEntity.setClient(client);

        try {
            articleRepository.save(articleEntity);
            log.info("Article created successfully with id: {}", articleEntity.getArticleId());
            return ArticleMapper.toDTO(articleEntity);
        } catch (Exception e) {
            log.error("Error saving article: {}", e.getMessage());
            throw new RuntimeException("Error saving article", e);
        }
    }

    @Override
    public ArticleDTO updateArticle(ArticleDTO articleDTO) {
        if (articleDTO == null) {
            throw new IllegalArgumentException("ArticleDTO cannot be null");
        }

        Long articleId = articleDTO.getArticleId();
        ArticleEntity articleEntity = articleRepository.findByArticleIdAndActiveTrue(articleId)
                .orElseThrow(() -> {
                    log.error("Article not found with id: {}", articleId);
                    return new NotFoundException("Article not found with id: " + articleId);
                });

        updateArticleEntityFromDTO(articleDTO, articleEntity);

        try {
            articleEntity = articleRepository.save(articleEntity);
            log.info("Article updated successfully with id: {}", articleEntity.getArticleId());
            return ArticleMapper.toDTO(articleEntity);
        } catch (Exception e) {
            log.error("Error updating article with id {}: {}", articleId, e.getMessage());
            throw new RuntimeException("Error updating article", e);
        }
    }

    private void updateArticleEntityFromDTO(ArticleDTO articleDTO, ArticleEntity articleEntity) {
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
        articleEntity.setLastUpdatedDate(articleDTO.getLastUpdatedDate());
        articleEntity.setUpdatedBy(articleDTO.getUpdatedBy());
    }

    @Override
    public void deleteArticle(Long articleId) {
        var articleEntity = articleRepository.findById(articleId).orElseThrow(() -> {
            log.error("Article not found with id: {}", articleId);
            return new IllegalArgumentException("Article not found");
        });

        articleEntity.setActive(false);

        try {
            articleRepository.save(articleEntity);
            log.info("Article deleted (deactivated) successfully with id: {}", articleEntity.getArticleId());
        } catch (Exception e) {
            log.error("Error deleting (deactivating) article: {}", e.getMessage());
            throw new RuntimeException("Error deleting (deactivating) article", e);
        }

        log.info("Article deleted successfully with id: {}", articleEntity.getArticleId());
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) {
        var articleEntity = articleRepository.findByArticleIdAndActiveTrue(articleId).orElseThrow(() -> {
            log.error("Active article not found with id: {}", articleId);
            return new IllegalArgumentException("Active article not found");
        });

        return ArticleMapper.toDTO(articleEntity);
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleMapper::toDTO)
                .collect(Collectors.toList());
    }
}
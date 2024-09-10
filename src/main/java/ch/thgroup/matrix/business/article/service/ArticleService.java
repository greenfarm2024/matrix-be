package ch.thgroup.matrix.business.article.service;

import ch.thgroup.matrix.business.article.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {
    ArticleDTO createArticle(ArticleDTO articleDTO);

    ArticleDTO getArticleById(Long articleId);

    ArticleDTO updateArticle(ArticleDTO articleDTO);

    void deleteArticle(Long articleId);

    List<ArticleDTO> getAllArticles();
}
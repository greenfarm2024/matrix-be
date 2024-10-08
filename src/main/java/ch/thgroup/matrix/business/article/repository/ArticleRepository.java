package ch.thgroup.matrix.business.article.repository;

import ch.thgroup.matrix.business.article.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    Optional<ArticleEntity> findByArticleIdAndActiveTrue(Long orderId);
}
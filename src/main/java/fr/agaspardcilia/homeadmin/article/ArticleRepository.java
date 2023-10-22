package fr.agaspardcilia.homeadmin.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findByCategory(ArticleCategory category);
    Set<Article> findByCategoryIn(Set<ArticleCategory> categories);
}

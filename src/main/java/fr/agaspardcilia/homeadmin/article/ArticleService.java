package fr.agaspardcilia.homeadmin.article;

import fr.agaspardcilia.homeadmin.article.dto.ArticleDto;
import fr.agaspardcilia.homeadmin.article.dto.ArticleUpdateDto;
import fr.agaspardcilia.homeadmin.article.dto.AvailableArticleDto;
import fr.agaspardcilia.homeadmin.common.util.AuthenticationUtil;
import fr.agaspardcilia.homeadmin.security.Permission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A service that manages {@link Article}s.
 */
@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;

    /**
     * Gets an article by its category.
     * Note that this should always return a value.
     *
     * @param category the category of the article.
     * @return the article, if found.
     * @throws UnauthorizedArticleAccessException if the user is not allowed to access the article.
     */
    public Optional<ArticleDto> getArticle(ArticleCategory category) throws UnauthorizedArticleAccessException {
        if (category.isAdminAccessOnly() && !canCurrentUserAccessAdminArticles()) {
            throw new UnauthorizedArticleAccessException();
        }

        return repository.findByCategory(category)
                .map(ArticleMapper::toDto);
    }

    /**
     * Updates an article.
     *
     * @param category the category of the article.
     * @param updateDto the update.
     * @return the updated article.
     */
    public ArticleDto updateArticle(ArticleCategory category, ArticleUpdateDto updateDto) {
        Article current = repository.findByCategory(category)
                .orElse(ArticleFactory.getEmpty(category));

        return ArticleMapper.toDto(
                repository.save(
                        ArticleMapper.update(current, updateDto)
                )
        );
    }


    /**
     * Gets all available articles name and category.
     *
     * @return the list of available articles ordered.
     */
    public List<AvailableArticleDto> getAvailableArticles() {
        boolean canAccessAdminArticles = canCurrentUserAccessAdminArticles();
        Set<ArticleCategory> availableCategories = Arrays.stream(ArticleCategory.values())
                .filter(e -> !e.isAdminAccessOnly() || canAccessAdminArticles)
                .collect(Collectors.toUnmodifiableSet());
        return repository.findByCategoryIn(availableCategories).stream()
                .sorted(Comparator.comparingInt(e -> e.getCategory().getOrder()))
                .map(ArticleMapper::toAvailableArticleDto)
                .toList();
    }

    private boolean canCurrentUserAccessAdminArticles() {
        return AuthenticationUtil.getCurrentUserPermissions().contains(Permission.ARTICLES_FETCH_ADMIN_ONLY);
    }


}

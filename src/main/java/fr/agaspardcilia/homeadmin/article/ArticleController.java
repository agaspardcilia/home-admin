package fr.agaspardcilia.homeadmin.article;

import fr.agaspardcilia.homeadmin.article.dto.ArticleDto;
import fr.agaspardcilia.homeadmin.article.dto.ArticleUpdateDto;
import fr.agaspardcilia.homeadmin.article.dto.AvailableArticleDto;
import fr.agaspardcilia.homeadmin.common.annotation.PermissionRequired;
import fr.agaspardcilia.homeadmin.common.annotation.PermitAll;
import fr.agaspardcilia.homeadmin.common.exception.ApiForbiddenException;
import fr.agaspardcilia.homeadmin.common.exception.ApiNotFoundException;
import fr.agaspardcilia.homeadmin.security.Permission;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService service;

    @PermitAll
    @GetMapping("/category/{category}")
    public ArticleDto getByCategory(@PathVariable ArticleCategory category) {
        try {
            return service.getArticle(category)
                    .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        } catch (UnauthorizedArticleAccessException e) {
            throw new ApiForbiddenException("Cannot access this article");
        }
    }

    @PermissionRequired(Permission.UPDATE_ARTICLE)
    @PostMapping("/category/{category}")
    public ArticleDto updateArticle(@PathVariable ArticleCategory category, @Valid @RequestBody ArticleUpdateDto updateDto) {
        return service.updateArticle(category, updateDto);
    }

    @PermitAll
    @GetMapping("/available")
    public List<AvailableArticleDto> getAvailable() {
        return service.getAvailableArticles();
    }
}

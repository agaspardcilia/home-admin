package fr.agaspardcilia.homeadmin.article.dto;

import fr.agaspardcilia.homeadmin.article.Article;
import io.jsonwebtoken.lang.Assert;
import jakarta.validation.constraints.NotNull;

/**
 * An {@link Article} update DTO.
 *
 * @param title the new title of the article.
 * @param content the new content of the article.
 */
public record ArticleUpdateDto(
        @NotNull
        String title,
        @NotNull
        String content
) {
    public ArticleUpdateDto {
        Assert.notNull(title);
        Assert.notNull(content);
    }
}

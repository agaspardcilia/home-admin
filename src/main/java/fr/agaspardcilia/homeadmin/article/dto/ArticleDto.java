package fr.agaspardcilia.homeadmin.article.dto;

import fr.agaspardcilia.homeadmin.article.Article;
import fr.agaspardcilia.homeadmin.article.ArticleCategory;

import java.time.Instant;
import java.util.UUID;

/**
 * A basic {@link Article} DTO.
 *
 * @param id the id of the article.
 * @param title the title of the article.
 * @param content the content of the article.
 * @param category the category of the article.
 * @param creationDate the creation date of the article.
 * @param updateDate the update date of the article.
 */
public record ArticleDto(
        UUID id,
        String title,
        String content,
        ArticleCategory category,
        Instant creationDate,
        Instant updateDate
) { }

package fr.agaspardcilia.homeadmin.article.dto;

import fr.agaspardcilia.homeadmin.article.ArticleCategory;

public record AvailableArticleDto(
        ArticleCategory category,
        String title
) { }

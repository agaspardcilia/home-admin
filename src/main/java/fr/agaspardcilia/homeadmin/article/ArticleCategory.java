package fr.agaspardcilia.homeadmin.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Articles category.
 */
@AllArgsConstructor
@Getter
public enum ArticleCategory {
    HOME(1, false),
    PUBLIC_LINKS(10, false),
    PRIVATE_LINKS(20, true);

    private final int order;
    private final boolean isAdminAccessOnly;
}

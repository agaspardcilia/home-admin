package fr.agaspardcilia.homeadmin.article;

/**
 * A factory centered around {@link Article}s.
 */
public class ArticleFactory {
    private ArticleFactory() {
        // Do not instantiate >:(
    }

    /**
     * Creates an empty article.
     *
     * @param category the category of the article.
     * @return the empty article.
     */
    public static Article getEmpty(ArticleCategory category) {
        return Article.builder()
                .category(category)
                .title("")
                .content("")
                .build();
    }
}

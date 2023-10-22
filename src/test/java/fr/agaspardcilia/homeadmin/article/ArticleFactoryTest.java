package fr.agaspardcilia.homeadmin.article;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleFactoryTest {

    @Test
    void testGetEmpty() {
        assertEquals(
                Article.builder()
                        .category(ArticleCategory.HOME)
                        .title("")
                        .content("")
                        .build(),
                ArticleFactory.getEmpty(ArticleCategory.HOME)
        );
    }
}

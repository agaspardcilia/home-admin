package fr.agaspardcilia.homeadmin.article;

import fr.agaspardcilia.homeadmin.article.dto.ArticleDto;
import fr.agaspardcilia.homeadmin.article.dto.ArticleUpdateDto;
import fr.agaspardcilia.homeadmin.article.dto.AvailableArticleDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArticleMapperTest {

    @Test
    void testToDto() {
        Article article = article();
        assertEquals(
                new ArticleDto(
                        article.getId(),
                        "foo",
                        "bar",
                        ArticleCategory.HOME,
                        Instant.MIN,
                        Instant.MAX
                ),
                ArticleMapper.toDto(
                        article
                )
        );
    }

    @Test
    void testMapToAvailable() {
        Article article = article();
        assertEquals(
                new AvailableArticleDto(article.getCategory(), article.getTitle()),
                ArticleMapper.toAvailableArticleDto(article)
        );
    }

    @Test
    void testUpdate() {
        Article article = article();
        ArticleUpdateDto updateDto = new ArticleUpdateDto("new foo", "new bar");
        ArticleMapper.update(article, updateDto);

        Article expected = article();
        expected.setId(article.getId());
        expected.setTitle(updateDto.title());
        expected.setContent(updateDto.content());
        assertEquals(expected, article);
    }

    private Article article() {
        return Article.builder()
                .id(UUID.randomUUID())
                .title("foo")
                .content("bar")
                .category(ArticleCategory.HOME)
                .creationDate(Instant.MIN)
                .updateDate(Instant.MAX)
                .build();
    }

}

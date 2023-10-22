package fr.agaspardcilia.homeadmin.article;

import fr.agaspardcilia.homeadmin.article.dto.ArticleUpdateDto;
import fr.agaspardcilia.homeadmin.article.dto.AvailableArticleDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository);
    }

    @AfterEach
    void tearDown() {
        clearAuthorities();
    }

    @Test
    void testGetRegular() throws UnauthorizedArticleAccessException {
        Article article = article();
        when(articleRepository.findByCategory(ArticleCategory.HOME))
                .thenReturn(Optional.of(article));
        assertEquals(
                Optional.of(ArticleMapper.toDto(article)),
                articleService.getArticle(ArticleCategory.HOME)
        );
    }

    @Test
    void testGetFailed() {
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new AnonymousAuthenticationToken(
                                "dayum", "boi", Set.of(new SimpleGrantedAuthority("dummy"))
                        )
                );
        try {
            articleService.getArticle(ArticleCategory.PRIVATE_LINKS);
            fail();
        } catch (UnauthorizedArticleAccessException e) {
            // Expected
        }

    }


    @Test
    void testUpdate() {
        Article article = article();
        ArticleUpdateDto update = new ArticleUpdateDto("new foo", "new bar");
        when(articleRepository.findByCategory(ArticleCategory.HOME))
                .thenReturn(Optional.of(article));
        Article expected = article();
        expected.setId(article.getId());
        expected.setTitle(update.title());
        expected.setContent(update.content());
        when(articleRepository.save(expected))
                .thenReturn(expected);

        assertEquals(
                ArticleMapper.toDto(expected),
                articleService.updateArticle(ArticleCategory.HOME, update)
        );
    }

    @Test
    void testGetAvailableEmpty() {
        when(articleRepository.findByCategoryIn(anySet()))
                .thenReturn(Set.of());
        setUserAuthorities("dummy");
        assertEquals(
                List.of(),
                articleService.getAvailableArticles()
        );
    }

    @Test
    void testGetAvailableNotAdmin() {
        Article home = article(ArticleCategory.HOME, "home");
        Article pubs = article(ArticleCategory.PUBLIC_LINKS, "pubs");

        when(articleRepository.findByCategoryIn(Set.of(ArticleCategory.HOME, ArticleCategory.PUBLIC_LINKS)))
                .thenReturn(Set.of(home, pubs));
        setUserAuthorities("USER");

        assertEquals(
                List.of(
                        new AvailableArticleDto(home.getCategory(), home.getTitle()),
                        new AvailableArticleDto(pubs.getCategory(), pubs.getTitle())
                ),
                articleService.getAvailableArticles()
        );
    }

    @Test
    void testGetAvailableAdmin() {
        Article home = article(ArticleCategory.HOME, "home");
        Article pubs = article(ArticleCategory.PUBLIC_LINKS, "pubs");
        Article privies = article(ArticleCategory.PRIVATE_LINKS, "privies");

        when(articleRepository.findByCategoryIn(
                Set.of(ArticleCategory.HOME, ArticleCategory.PUBLIC_LINKS, ArticleCategory.PRIVATE_LINKS))
        ).thenReturn(Set.of(home, pubs, privies));
        setUserAuthorities("ADMIN");

        assertEquals(
                List.of(
                        new AvailableArticleDto(home.getCategory(), home.getTitle()),
                        new AvailableArticleDto(pubs.getCategory(), pubs.getTitle()),
                        new AvailableArticleDto(privies.getCategory(), privies.getTitle())
                ),
                articleService.getAvailableArticles()
        );
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

    private Article article(ArticleCategory category, String title) {
        return Article.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content("bar")
                .category(category)
                .creationDate(Instant.MIN)
                .updateDate(Instant.MAX)
                .build();
    }

    private void setUserAuthorities(String... roles) {
        Set<SimpleGrantedAuthority> auths = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
        SecurityContextHolder.getContext()
                .setAuthentication(new AnonymousAuthenticationToken("dayum", "boi", auths));
    }

    private void clearAuthorities() {
        SecurityContextHolder.clearContext();
    }
}

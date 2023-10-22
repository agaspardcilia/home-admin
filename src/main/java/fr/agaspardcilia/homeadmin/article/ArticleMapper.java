package fr.agaspardcilia.homeadmin.article;

import fr.agaspardcilia.homeadmin.article.dto.ArticleDto;
import fr.agaspardcilia.homeadmin.article.dto.ArticleUpdateDto;
import fr.agaspardcilia.homeadmin.article.dto.AvailableArticleDto;
import org.apache.commons.text.StringEscapeUtils;

/**
 * A mapper centered around {@link Article}s.
 */
public class ArticleMapper {

    private ArticleMapper() {
        // Do not instantiate! >:(
    }

    /**
     * Maps an article to a DTO.
     *
     * @param article the article to map.
     * @return the DTO.
     */
    public static ArticleDto toDto(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCategory(),
                article.getCreationDate(),
                article.getUpdateDate()
        );
    }

    /**
     * Maps an article to an available article DTO.
     *
     * @param article the article to map.
     * @return the DTO.
     */
    public static AvailableArticleDto toAvailableArticleDto(Article article) {
        return new AvailableArticleDto(
                article.getCategory(),
                article.getTitle()
        );
    }


    /**
     * Updates a given article with an update DTO.
     * Note that this method won't create a new instance of article.
     * Note' that this will escape the content and the title to prevent injection.
     *
     * @param current the article to update.
     * @param updateDto the update data.
     * @return the article passed in parameter.
     */
    public static Article update(Article current, ArticleUpdateDto updateDto) {
        current.setTitle(StringEscapeUtils.escapeHtml4(updateDto.title()));
        current.setContent(StringEscapeUtils.escapeHtml4(updateDto.content()));
        return current;
    }

}

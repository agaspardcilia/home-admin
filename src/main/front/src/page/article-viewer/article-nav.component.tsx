import React from 'react';
import { AvailableArticle } from '../../shared/model/article/available-article.model';
import { ArticleCategory } from '../../shared/model/article/article-category.model';

interface ArticleNavProps {
    loading: boolean;
    availableArticles?: AvailableArticle[];
    onArticleChange: (category: ArticleCategory) => void;
}

export const ArticleNavComponent: React.FC<ArticleNavProps> = ({ loading, availableArticles, onArticleChange }: ArticleNavProps) => {

    const articleChange = (category: ArticleCategory) => () => onArticleChange(category);

    const renderNavElements = () => {
        if (!availableArticles) {
            return (<div>No element to display, this should not happen!</div>); // TODO: handle this!
        }

        const renderedArticles = () =>  availableArticles.map(a => (
            <li key={a.category}>
                <a onClick={articleChange(a.category)}>{a.title}</a>
            </li>
        ));

        return <ul>{renderedArticles()}</ul>
    }

    return (
        <>
            { loading ? <div>This should be a skeleton or some shit.</div> : renderNavElements() }
        </>
    );
};

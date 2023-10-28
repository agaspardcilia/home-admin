import { Result } from '../model/result.model';
import { AvailableArticle } from '../model/article/available-article.model';
import { Http } from '../util/http.util';
import { ArticleCategory } from '../model/article/article-category.model';
import { Article } from '../model/article/article.model';

const basePath = '/articles';

export const articlesApi = {
    getAvailable : (): Promise<Result<AvailableArticle[]>> => Http.get<AvailableArticle[]>(`${basePath}/available`),
    getByCategory : (category: ArticleCategory): Promise<Result<Article>> => Http.get<Article>(`${basePath}/category/${category}`),
    updateArticle: (article: Article): Promise<Result<Article>> => {
        const { category, title, content } = article;
        const body = { title, content };
        return Http.post<Article>(`${basePath}/category/${category}`, body);
    }
};

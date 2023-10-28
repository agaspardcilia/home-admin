import { ArticleCategory } from './article-category.model';

export interface Article {
    id: string;
    title: string;
    content: string;
    category: ArticleCategory;
    creationDate: string;
    updateDate: string;
}

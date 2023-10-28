import { AvailableArticle } from '../model/article/available-article.model';
import { Article } from '../model/article/article.model';
import { ArticleCategory } from '../model/article/article-category.model';
import { create } from 'zustand';
import { articlesApi } from '../api/articles.api';

/**
 * TODO: comment me!
 */
interface ArticlesStore {
    available?: AvailableArticle[];
    availableLoading: boolean;
    fetchAvailable: () => Promise<AvailableArticle[]>;

    current?: Article;
    currentLoading: boolean;
    fetchArticle: (category: ArticleCategory) => Promise<void>;
    updateCurrentArticle: (article: Article) => Promise<Article | null>;
}

export const useArticlesStore = create<ArticlesStore>(
    (set) => ({
        available: undefined,
        availableLoading: false,
        fetchAvailable: async () => {
            const result: AvailableArticle[] = [];
            set({ availableLoading: true });
            const response = await articlesApi.getAvailable();
            if (response.status === 'success') {
                result.push(...response.result);

            } else {
                // TODO: should show a toast or something.
            }

            set({ available: result, availableLoading: false });
            return result;
        },

        current: undefined,
        currentLoading: false,
        fetchArticle: async (category: ArticleCategory) => {
            set({ currentLoading: true });
            const response = await articlesApi.getByCategory(category);
            if (response.status === 'success') {
                set({ current: response.result});
            } else {
                set({ current: undefined});
            }

            set({ currentLoading: false });
        },
        updateCurrentArticle: async (article: Article) => {
            const response = await articlesApi.updateArticle(article);

            if (response.status === 'success') {
                const { result } = response;
                set({ current: result })
                return result;
            } else {
                return null; // TODO: handle error!
            }
        },
    })
);

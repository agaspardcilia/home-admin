import React, { useState } from 'react';
import { ArticleNavComponent } from './article-nav.component';
import { useArticlesStore } from '../../shared/store/articles.store';
import { ArticleCategory } from '../../shared/model/article/article-category.model';
import { useAuthenticationStore } from '../../shared/store/authentication.store';
import { ArticleEditorComponent } from './article-editor.component';
import { Article } from '../../shared/model/article/article.model';
import { ArticleViewerComponent } from './article-viewer.component';

export const ArticleComponent: React.FC = () => {
    const articles = useArticlesStore();
    const isAdmin = useAuthenticationStore(state => state.isAdmin);
    const [editMode, setEditMode] = useState<boolean>(false);

    const onArticleChange = (category: ArticleCategory): void => {
        articles.fetchArticle(category);
    };

    const switchEditMode = (): void => setEditMode(!editMode);

    const renderEditButton = () => {
        console.log('editMode', editMode);
        return (
            <>
                {!editMode
                    ? <button onClick={switchEditMode}>Edit</button>
                    : undefined}
            </>
        );
    };

    const onArticleSave = (article: Article) => {
        console.log('article being saved', article);
        // TODO: should show a toast or something!
        const hasTitleChanged = !articles.current || articles.current.title !== article.title;

        articles.updateCurrentArticle(article)
            .then(async (res) => {
                if (hasTitleChanged) {
                    await articles.fetchAvailable()
                }
                switchEditMode();
            });
    };

    const renderArticle = () => {
      const article = articles.current;

      if (!article) {
          return 'No article to be found!';
      }

      return (
          <>
              {isAdmin() ? renderEditButton() : undefined}
              {editMode
                  ? <ArticleEditorComponent article={article} onSave={onArticleSave} onClose={switchEditMode} />
                  : <ArticleViewerComponent article={article} /> }
          </>
      );
    };

    return (
        <div>
            <ArticleNavComponent loading={articles.availableLoading}
                                 availableArticles={articles.available}
                                 onArticleChange={onArticleChange}/>
            { articles.currentLoading ? 'Loading m8' : renderArticle() }
        </div>
    );
};

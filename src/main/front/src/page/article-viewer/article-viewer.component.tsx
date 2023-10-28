import React from 'react';
import { Article } from '../../shared/model/article/article.model';
import Markdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

interface ArticleViewerProps {
    article: Article;
}

export const ArticleViewerComponent: React.FC<ArticleViewerProps> = ({article}) => (
    <Markdown remarkPlugins={[remarkGfm]}>{article.content}</Markdown>
);

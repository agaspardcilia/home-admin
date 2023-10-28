import React, { useState } from 'react';
import { Article } from '../../shared/model/article/article.model';
import MDEditor from '@uiw/react-md-editor';

interface ArticleEditorProps {
    article: Article;
    onSave: (article: Article) => void;
    onClose: () => void;
}

export const ArticleEditorComponent: React.FC<ArticleEditorProps> = ({ article, onSave, onClose }) => {
    const [title, setTitle] = useState<string>(article.title);
    const [content, setContent] = useState<string>(article.content);

    const onContentChange = (change: any): void => {
        console.log('content change', change);
        setContent(change);
    }

    const onTitleChange = (change: any): void => {
        console.log('title change', change);
        setTitle(change.target.value);
    }

    const saveAction = () => {
        const toSave: Article = {
            ...article,
            title,
            content,
        };

        onSave(toSave);
    };

    const cancelAction = () => onClose();

    return (
        <div>
            <button onClick={saveAction}>Save</button>
            <button onClick={cancelAction}>Cancel</button>
            <input type="text" value={title} onChange={onTitleChange} />
            <MDEditor value={content} onChange={onContentChange} />
        </div>
    );
};

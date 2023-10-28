import React, { useEffect } from 'react';
import { useAuthenticationStore } from './shared/store/authentication.store';
import { useArticlesStore } from './shared/store/articles.store';

interface StartupProps {
    onStartupComplete: () => void;
}

export const Startup: React.FC<StartupProps> = ({ onStartupComplete }) => {
    const authentication = useAuthenticationStore();
    const articles = useArticlesStore();

    const loadStartupUser = async () => {
        const jwt = authentication.loadLocalJwt();

        if (jwt) {
            console.log('fetching current user');
            await authentication.fetchCurrentUser();
        } else {
            console.log('no jwt');
        }
    };

    const loadAvailableArticles = async () => {
        const { fetchAvailable, fetchArticle } = articles;
        const available = await fetchAvailable();

        if (!available || !available.length) {
            console.log('not fetching first available', available);
            return;
        }

        await fetchArticle(available[0].category);
    };

    console.log('Startup render');
    // TODO: this is being loaded twice on startup, figure out why.
    useEffect(() => {
        loadStartupUser()
            .then(() => loadAvailableArticles())
            .then(() => onStartupComplete())
            .catch(console.log);
    }, []);


    return <React.Fragment />;
};

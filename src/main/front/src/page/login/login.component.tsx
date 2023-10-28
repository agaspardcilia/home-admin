import React, { useState } from 'react';
import { useAuthenticationStore } from '../../shared/store/authentication.store';
import { useArticlesStore } from '../../shared/store/articles.store';

interface LoginFields {
    mail?: string;
    password?: string;
    rememberMe: boolean;
}

/**
 * TODO: comment me!
 * @constructor
 */
export const Login: React.FC = () => {
    const authentication = useAuthenticationStore();
    const fetchAvailableArticles = useArticlesStore(state => state.fetchAvailable);
    const [fields, setFields] = useState<LoginFields>({
        rememberMe: true
    });
    const [ loginResult, setLoginResult ] = useState<string>("No login attempt");
    const onSubmit = async () => {
        const {mail, password, rememberMe} = fields;

        if (mail != null && password != null) {
            setLoginResult('Login in progress');
            const result = await authentication.authenticate(mail, password, rememberMe);
            if (result) {
                setLoginResult('Login success');
                await authentication.fetchCurrentUser();
                await fetchAvailableArticles();
            } else {
                setLoginResult('Login failure');
            }
        } else {
            setLoginResult('Invalid form');
        }

    };

    const onInputChange = (field: keyof LoginFields) => (change: any) =>
        setFields(prev => ({ ...prev, [field]: change.target.value }));

    const onRememberMeClick = () =>
        setFields(prev => ({ ...prev, rememberMe: !prev.rememberMe }));

    return (
        <div>
            Login<br />
            <input type="email" placeholder="mail" value={fields.mail} onChange={onInputChange('mail')} /><br/>
            <input type="password" placeholder="password" value={fields.password} onChange={onInputChange('password')} /><br/>
            <label>Remember me?</label><input type="checkbox" checked={fields.rememberMe} onClick={onRememberMeClick} /><br/>
            <button type="submit" onClick={onSubmit}>Login</button>
            <p>&nbsp;</p>
            <p>Login status: { loginResult }</p>
            <p>Current user: { JSON.stringify(authentication.user) }</p>
            <p>Jwt: { authentication.jwt }</p>
        </div>
    );
};

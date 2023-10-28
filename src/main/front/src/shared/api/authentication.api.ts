import { Http } from '../util/http.util';
import { Result } from '../model/result.model';

interface AuthenticateResult {
    'id_token': string;
}

export const authenticationApi = {
    authenticate: (mail: string, password: string, rememberMe: boolean): Promise<Result<AuthenticateResult>> =>
        Http.post<AuthenticateResult>('/authenticate', { mail, password, rememberMe })
};

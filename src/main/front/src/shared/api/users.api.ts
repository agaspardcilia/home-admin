import { User } from '../model/user.model';
import { Http } from '../util/http.util';
import { Result } from '../model/result.model';

const basePath = '/users';

export const usersApi = {
    getCurrentUser: (): Promise<Result<User>> => {
        return Http.get<User>(`${basePath}/current`);
    }
};

import { create } from 'zustand';
import { User } from '../model/user.model';
import { authenticationApi } from '../api/authentication.api';
import { usersApi } from '../api/users.api';
import { localStorageUtil } from '../util/local-storage.util';

// TODO: comment me!
interface AuthenticationState {
    user?: User;
    jwt?: string; // TODO: probably doesn't need to have this here!
    authenticate: (mail: string, password: string, rememberMe: boolean) => Promise<boolean>;
    fetchCurrentUser: () => Promise<void>;
    loadLocalJwt: () => string | null;
    clearJwt: () => void;
    isAdmin: () => boolean;
}

const readJwtFromLocalStorage = (): string | null => localStorageUtil.read('jwt');
const writeJwtToLocalStorage = (jwt: string): void => localStorageUtil.write('jwt', jwt);
const clearLocalStorage = (): void => localStorageUtil.remove('jwt');
// TODO: test me!
// TODO: use devtools
export const useAuthenticationStore = create<AuthenticationState>(
    (set, get) => ({
        user: undefined,
        authenticate: async (mail, password, rememberMe) => {
            const response = await authenticationApi.authenticate(mail, password, rememberMe);

            if (response.status === 'success') {
                const jwt = response.result.id_token;
                set({ jwt });
                writeJwtToLocalStorage(jwt);
                return true;
            } else {
                clearLocalStorage();
                return false;
            }
        },
        fetchCurrentUser: async () => {
            try {
                const response = await usersApi.getCurrentUser();

                if (response.status === 'success') {
                    set({ user: response.result});
                } else {
                    set({ jwt: undefined, user: undefined });
                }
            } catch (exception) {
                set({ jwt: undefined, user: undefined });
            }

        },
        loadLocalJwt: () => {
            const jwt = readJwtFromLocalStorage();
            set({ jwt: jwt ?? undefined });
            return jwt;
        },
        clearJwt: () => set({ jwt: undefined, user: undefined }),
        isAdmin: () => {
            const user = get().user;

            return !!user && user.isAdmin;
        }
    })
);


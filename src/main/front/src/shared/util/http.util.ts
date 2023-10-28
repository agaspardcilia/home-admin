import { Result } from '../model/result.model';
import { applicationEnv } from './env.util';
import { localStorageUtil } from './local-storage.util';

interface ErrorResponse {
    httpStatus: string;
    httpStatusCode: number;
    message: string;
    timestamp: string;
}

type MethodType = 'GET' | 'POST' | 'PUT';
// TODO: type me!
const getPath = (path: string): string => applicationEnv.apiBasePath + path;
const performRequest = async <R>(path: string, method: MethodType, body: any): Promise<Result<R>> => {
    const response = await fetch(
        getPath(path),
        {
            method: method,
            mode: 'cors',
            headers: {
                'Content-type': 'application/json;charset=UTF-8',
                'Authorization': `Bearer ${localStorageUtil.read('jwt')}`
            },
            body: body ? JSON.stringify(body) : undefined
        }
    );

    if (response.ok) {
        const body = await response.json() as R;
        return {
            status: 'success',
            result: body
        };
    } else {
        const body = await response.json() as ErrorResponse;
        return { status: 'failure', ...body };
    }
};

const performGet = async <R>(path: string): Promise<Result<R>> => performRequest(path, 'GET', undefined);
const performPost = async <R>(path: string, body: any = {}): Promise<Result<R>> => performRequest(path, 'POST', body);

export const Http = {
    get: performGet,
    post: performPost
};

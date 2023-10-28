import { Result } from '../model/result.model';
import { Action } from '../model/action/action.model';
import { Http } from '../util/http.util';
import { ActionExecutionResult } from '../model/action/action-execution-result.model';

const basePath = '/actions';

export const actionsApi = {
    scan: (): Promise<Result<Action[]>> => Http.post(`${basePath}/scan`),
    getAll: (): Promise<Result<Action[]>> => Http.get(`${basePath}`),
    run: (id: string): Promise<Result<ActionExecutionResult>> => Http.post(`${basePath}/run/${id}`)
};

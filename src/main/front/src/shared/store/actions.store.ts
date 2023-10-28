import { Action } from '../model/action/action.model';
import { create } from 'zustand';
import { actionsApi } from '../api/actions.api';
import { ActionExecutionResult } from '../model/action/action-execution-result.model';

interface ActionsStore {
    actions?: Action[];
    loading: boolean;
    actionInExecution: boolean;

    scan: () => Promise<void>;
    fetchAll: () => Promise<void>;
    run: (id: string) => Promise<ActionExecutionResult | undefined>;
}

export const useActionsStore = create<ActionsStore>(
    (set) => ({
        actions: undefined,
        loading: false,
        actionInExecution: false,

        scan: async () => {
            set({ loading: true });
            const response = await actionsApi.scan();
            if (response.status === 'success') {
                set({ actions: response.result });
            } else {
                // TODO: handle errors!
            }

            set({ loading: false });
        },
        fetchAll: async () => {
            set({ loading: true });
            const result: Action[] = [];
            const response = await actionsApi.getAll();
            if (response.status === 'success') {
                result.push(...response.result);
            } else {
                // TODO: handle errors!
            }

            set({ loading: false, actions: result });
        },
        run: async (id: string) => {
            set({ actionInExecution: true });
            const response = await actionsApi.run(id);
            if (response.status === 'success') {
                set({ actionInExecution: false });
                return response.result;
            }
            // TODO: handle errors!

            set({ actionInExecution: false });
            return undefined;
        }
    })
);

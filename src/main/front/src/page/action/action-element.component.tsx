import React, { useState } from 'react';
import { Action } from '../../shared/model/action/action.model';
import { useActionsStore } from '../../shared/store/actions.store';
import { ActionExecutionResult } from '../../shared/model/action/action-execution-result.model';

interface ActionElementProps {
    action: Action;
}
export const ActionElementComponent: React.FC<ActionElementProps> = ({action}) => {
    const actionsStore = useActionsStore();
    const [execResult, setExecResult] = useState<ActionExecutionResult>();
    const run = () => {
        actionsStore.run(action.id)
            .then(setExecResult)
    };


    return (
        <div>
            <span>{action.name} {JSON.stringify(action.runnableExists)} {action.creationDate} {action.updateDate}</span><br />
            <button onClick={run} disabled={actionsStore.actionInExecution || !action.runnableExists}>
                {actionsStore.actionInExecution ? 'Execution in progress' : 'Execute'}
            </button>
            <br />
            { execResult ? JSON.stringify(execResult) : undefined }
        </div>
    );
};

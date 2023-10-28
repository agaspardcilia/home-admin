import React, { useEffect } from 'react';
import { useActionsStore } from '../../shared/store/actions.store';
import { ActionElementComponent } from './action-element.component';

export const ActionListComponent: React.FC = () => {
    const actionsStore = useActionsStore();

    useEffect(() => {
        actionsStore.fetchAll();
    }, []);

    const renderActionList = () => {
        const { actions } = actionsStore;

        if (!actions || !actions.length) {
            return 'No available action';
        }
        console.log('actions', actions);
        const renderedActions = actions
            .map(a => (
                    <li key={a.id}>
                        <ActionElementComponent action={a}/>
                    </li>
                )
            );
        return <ul>{renderedActions}</ul>;
    };

    const onScan = (): void => {
        actionsStore.scan();
    }

    const renderActionListAndControls = () => (
        <div>
            <button onClick={onScan}>Scan</button> <br />
            {renderActionList()}
        </div>
    );

    return (
        <div>
            <h3>Actions</h3>
            {
                actionsStore.loading
                    ? 'Loading...'
                    : renderActionListAndControls()
            }

        </div>
    );
};

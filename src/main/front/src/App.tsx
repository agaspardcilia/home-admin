import React, { ReactElement, useState } from 'react';
import './App.css';
import { Startup } from './startup.component';
import { Header } from './layout/header.component';
import { NavBar } from './layout/navbar.component';
import { AppRoutes } from './shared/router';

const App = (): ReactElement => {
    const [startingUp, setStartingUp] = useState<boolean>(true);

    const onStartupComplete = (): void => setStartingUp(false);

    return (
        <>
            <Startup onStartupComplete={onStartupComplete}/>
            {!startingUp ? (
                <>
                    <Header/>
                    <NavBar/>
                    <AppRoutes />
                </>
            ): undefined}
        </>
    );
};

export default App;

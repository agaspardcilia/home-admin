import { Route, Routes } from 'react-router-dom';
import { Home } from '../page/home/home.component';
import { Login } from '../page/login/login.component';
import React from 'react';
import { ActionListComponent } from '../page/action/action-list.component';

export const AppRoutes = () => (
    <Routes>
        <Route path="/">
            <Route index element={<Home />}/>
            <Route path="actions" element={<ActionListComponent />}/>
            <Route path="login" element={<Login />}/>
        </Route>
    </Routes>
);

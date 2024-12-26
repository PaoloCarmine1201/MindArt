import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import GestioneBambini from "./pages/GestioneBambini";
import { configureStore } from '@reduxjs/toolkit'
import DettaglioBambino from "./pages/DettagliBambino";
import EventiTeraputa from "./pages/EventiTeraputa";
import Login from "./pages/login/Login";
import Registration from "./pages/registration/Registration";
import ChildLogin from "./pages/ChildLogin";
import HomePage from "./pages/HomePage";
import ProtectedRoute from "./auth/ProtectedRoute";
import {AuthProvider} from "./auth/AuthProvider";
import DisegnaBambino from "./pages/DisegnaBambino";
import ProtectedRouteChild from "./auth/ProtectedRouteChild";

const router = createBrowserRouter([
    {
        path: '/',
        name: "Home",
        element: <ProtectedRoute> <App /> </ProtectedRoute>
    },
    {
        path: '/gestioneBambini',
        name: "Gestione Bambini",
        element: <ProtectedRoute> <GestioneBambini/> </ProtectedRoute>
    },
    {
        path: '/aggiungiBambino',
        name: 'Aggiungi Bambino',
        element: <ProtectedRoute> <GestioneBambini/> </ProtectedRoute>
    },
    {
        path: '/dettaglioBambino/:id',
        name: 'Dettaglio',
        element: <ProtectedRoute> <DettaglioBambino/> </ProtectedRoute>
    },
    {
        path: '/calendarioEventi',
        name:'Calendario',
        element: <ProtectedRoute> <EventiTeraputa/> </ProtectedRoute>
    },
    {
        path:"/login",
        name: "Login",
        element : <Login />
    },
    {
        path:"/register",
        name: "Registrazione",
        element :<ProtectedRoute>  <Registration /></ProtectedRoute>
        },
    {
        path:"/childlogin",
        name: "Child Login",
        element : <ChildLogin />
    },
    {
        path: "child/draw",
        name: "Child Drawing",
        element:<ProtectedRouteChild><DisegnaBambino/></ProtectedRouteChild>
    },
    {
        path:"/home",
        name: "Home",
        element : <ProtectedRoute><HomePage /></ProtectedRoute>
    }
])

const store = configureStore({reducer : () => ({})})

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <AuthProvider>
      <React.StrictMode>
        <Provider store={store}>
            <RouterProvider router={router}/>
        </Provider>
      </React.StrictMode>
    </AuthProvider>
);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

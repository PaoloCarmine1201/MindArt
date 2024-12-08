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

const router = createBrowserRouter([
    {
        path: '/',
        name: "Home",
        element: <App />
    },
    {
        path: '/gestioneBambini',
        name: "Gestione Bambini",
        element: <GestioneBambini/>
    },
    {
        path: '/aggiungiBambino',
        name: 'Aggiungi Bambino',
        element: <GestioneBambini/>
    },
    {
        path: '/dettaglioBambino/:id',
        name: 'Dettaglio',
        element: <DettaglioBambino/>
    },
    {
        path: '/calendarioEventi',
        name:'Calendario',
        element: <EventiTeraputa/>
    }
])

const store = configureStore({reducer : () => ({})})

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
        <RouterProvider router={router}/>
    </Provider>
  </React.StrictMode>
);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

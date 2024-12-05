import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import GestioneBambiniComponents from "./components/GestioneBambiniComponents";
import { configureStore } from '@reduxjs/toolkit'
import DettaglioBambinoComponent from "./components/DettaglioBambinoComponent";

const router = createBrowserRouter([
    {
        path: '/',
        name: "Home",
        element: <App />
    },
    {
        path: '/gestioneBambini',
        name: "Gestione Bambini",
        element: <GestioneBambiniComponents />
    },
    {
        path: '/aggiungiBambino',
        name: 'Aggiungi Bambino',
        element: <GestioneBambiniComponents/>
    },
    {
        path: '/dettaglioBambino/:id',
        name: 'Dettaglio',
        element: <DettaglioBambinoComponent/>
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

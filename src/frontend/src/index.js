import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { configureStore } from '@reduxjs/toolkit';
import App from './App';
import GestioneBambini from "./pages/GestioneBambini";
import DettaglioBambino from "./pages/DettagliBambino";
import EventiTeraputa from "./pages/EventiTeraputa";
import Login from "./pages/login/Login";
import Registration from "./pages/registration/Registration";
import ChildLogin from "./pages/ChildLogin";
import HomePage from "./pages/HomePage";
import ProtectedRoute from "./auth/ProtectedRoute";
import { AuthProvider } from "./auth/AuthProvider";
import ProtectedRouteChild from "./auth/ProtectedRouteChild";
import DisegnaBambino from "./pages/DisegnaBambino";
import GestioneMaterialeFull from "./components/GestioneMateriale/GestioneMaterialeFull";
import ProfiloTerapeuta from "./pages/ProfiloTerapeuta";
import VisualizzazioneMateriale from "./pages/VisualizzazioneMateriale";
import DisegnoInCorso from "./pages/DisegnoInCorsoTerapeuta";
import GestioneListaDisegno from "./components/GestioneListaDisegno/GestioneListaDisegno";
import ColoraBambino from "./pages/ColoraBambino";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />, // Usa App come layout principale per tutte le pagine
        children: [
            // Rotte protette
            {
                path: "/",
                element: (
                    <ProtectedRoute>
                        <HomePage />
                    </ProtectedRoute>
                )
            },
            {
                path: "/gestioneBambini",
                element: (
                    <ProtectedRoute>
                        <GestioneBambini />
                    </ProtectedRoute>
                )
            },
            {
                path: "/aggiungiBambino",
                element: (
                    <ProtectedRoute>
                        <GestioneBambini />
                    </ProtectedRoute>
                )
            },
            {
                path: "/dettaglioBambino/:id",
                element: (
                    <ProtectedRoute>
                        <DettaglioBambino />
                    </ProtectedRoute>
                )
            },
            {
                path: "/dettaglioDisegni/:id",
                element: (
                    <ProtectedRoute>
                        <GestioneListaDisegno />
                    </ProtectedRoute>
                )
            },
            {
                path: "/calendarioEventi",
                element: (
                    <ProtectedRoute>
                        <EventiTeraputa />
                    </ProtectedRoute>
                )
            },
            {
                path: "/visualizzazioneMateriale",
                element: (
                    <ProtectedRoute>
                        <VisualizzazioneMateriale />
                    </ProtectedRoute>
                )
            },
            {
                path: "/gestioneMateriale",
                element: (
                    <ProtectedRoute>
                        <GestioneMaterialeFull />
                    </ProtectedRoute>
                )
            },
            {
                path: "/profilo",
                element: (
                    <ProtectedRoute>
                        <ProfiloTerapeuta />
                    </ProtectedRoute>
                )
            },
            {
                path: "/terapeuta/draw",
                element: (
                    <ProtectedRoute>
                        <DisegnoInCorso />
                    </ProtectedRoute>
                )
            },
            {
                path: "/child/draw",
                element: (
                    <ProtectedRouteChild>
                        <DisegnaBambino />
                    </ProtectedRouteChild>
                )
            },
            {
              path: "/child/colore",
                element: (
                    <ProtectedRouteChild>
                        <ColoraBambino />
                    </ProtectedRouteChild>
                )
            },

            // Rotte non protette
            {
                path: "/login",
                element: <Login />
            },
            {
                path: "/register",
                element: <Registration />
            },
            {
                path: "/childlogin",
                element: <ChildLogin />
            }
        ]
    }
]);


const store = configureStore({ reducer: () => ({}) });

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <AuthProvider>
        <React.StrictMode>
            <Provider store={store}>
                <RouterProvider router={router} />
            </Provider>
        </React.StrictMode>
    </AuthProvider>
);

reportWebVitals();

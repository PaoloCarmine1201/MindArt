import React from 'react';
import { ToastContainer } from 'react-toastify';
import { Outlet } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
      <div>
          <ToastContainer
              position="bottom-right" // Posizione del toast
              autoClose={1000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false} // Layout da sinistra a destra
              pauseOnFocusLoss // Pausa quando la finestra perde il focus
              draggable // Abilita il trascinamento
              pauseOnHover // Pausa quando il mouse passa sopra il toast
              limit={1}
              l
          />

          <Outlet />
      </div>
  );
}

export default App;

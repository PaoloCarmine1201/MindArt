import React from 'react';
import { ToastContainer } from 'react-bootstrap';

const ToastNotification = () => {
    return (
        <ToastContainer
            position="bottom-right"
            autoClose={3000} // 3 secondi di durata
            hideProgressBar={false} // Mostra la barra di progresso
            newestOnTop={true} // Mostra le notifiche più recenti in cima
            closeOnClick // Chiudi al clic
            pauseOnFocusLoss // Pausa se l'utente cambia finestra
            draggable // Consente di trascinare il toast
            pauseOnHover // Pausa al passaggio del mouse
        />
    );
};

export default ToastNotification;

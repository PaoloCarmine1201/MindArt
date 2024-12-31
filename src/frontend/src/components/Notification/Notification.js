import React from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';

const ToastNotification = ({ show, title, message, type, closeCallback}) => {
    return (
        <ToastContainer position="top-end" className="p-3">
            <Toast
                show={show}
                onClose={closeCallback}
                bg={type}
                delay={3000} // Nascondi automaticamente dopo 3 secondi
                autohide
            >
                <Toast.Header>
                    <strong className="me-auto">{title}</strong>
                    <small>Ora</small>
                </Toast.Header>
                <Toast.Body>{message}</Toast.Body>
            </Toast>
        </ToastContainer>
    );
};

export default ToastNotification;

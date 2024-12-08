import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useRef, useState} from "react";
import RegisterBambinoValidatedForm from "./RegisterBambinoValidatedForm";

function RegisterBambino(){

    const [show, setShow] = useState(false);

    const formRef = useRef(null);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = async (values) => {

        const terapeutaId = 1;

        // TODO Recupera l'id del terapeuta loggato e generare codice bimbo
        const payload = {
            ...values,
            terapeutaId, // Aggiungi l'id del terapeuta
            codice: 'BGYHHU'// Genera un codice univoco
        };
        try {
            console.log('Dati inviati:', values);
            // Effettua la chiamata POST al server
            const response = await fetch('http://localhost:8080/api/bambino/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                throw new Error('Errore durante l\'invio dei dati');
            }

            alert('Form inviato con successo!');
            handleClose(); // Chiudi il modale
        } catch (error) {
            console.error('Errore:', error);
            alert('Si è verificato un errore.');
        }
    };

    return (
        <>
            <Button variant="primary" onClick={handleShow}>Aggiungi Bambino</Button>
            <Modal show={show}
                   onHide={handleClose}
                   backdrop="static"
                   keyboard={false}
                   aria-labelledby="contained-modal-title-vcenter"
                   centered
            >
                <Modal.Header >
                    <Modal.Title>Registra un bambino</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <RegisterBambinoValidatedForm
                        handleSubmit={handleSubmit}
                        formRef={formRef}
                    ></RegisterBambinoValidatedForm>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Annulla</Button>
                    <Button variant="primary" onClick={() => formRef.current.submitForm()}>Conferma</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default RegisterBambino;
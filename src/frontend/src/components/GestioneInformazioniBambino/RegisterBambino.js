import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useRef, useState} from "react";
import {toast} from "react-toastify";
import InformazioniBambinoValidatedForm from "./InformazioniBambinoValidatedForm";
import "../../style/RegisterBambino.css";
import "../../style/Button.css";
import "../../style/Modal.css";
import axiosInstance from "../../config/axiosInstance";


function RegisterBambino(){

// State to control modal visibility
    const [show, setShow] = useState(false);

    // Reference to the form for programmatic submission
    const formRef = useRef(null);

    // Function to close the modal
    const handleClose = () => setShow(false);

    // Function to open the modal
    const handleShow = () => setShow(true);


    // Function to handle form submission
    const handleSubmit = async (values) => {

        const payload = {
            ...values,
        };

        try {
            console.log('Dati inviati:', payload);

            // Make a POST request to add a new bambino
            const response = await axiosInstance.post('/api/terapeuta/bambino/add', payload);

            // Check if the response status indicates success
            if (response.status === 201 || response.status === 200) {
                console.log('Bambino registrato con successo:', response.data);
                toast.success(
                    'Bambino registrato con successo!',
                    {
                        position: 'bottom-right'
                    }
                );
                handleClose(); // Close the modal upon success
                // Optionally, you can reset the form or perform additional actions here
            } else {
                console.log('Errore richiesta:', response);
                toast.error(
                    'Si è verificato un errore durante la registrazione del bambino.',
                    {
                        position: 'bottom-right'
                    }
                );
            }
        } catch (error) {
            console.error('Errore:', error);
            // Extract error message from response if available
            const errorMessage = error.response?.data?.message || 'Si è verificato un errore durante la registrazione del bambino.';
            toast.error(
                errorMessage,
                {
                    position: 'bottom-right'
                }
            );
        }
    };


    return (
        <>
            <Button variant="btn-outline-primary btn-conferma" onClick={handleShow}>Aggiungi Bambino</Button>
            <Modal show={show}
                   onHide={handleClose}
                   backdropClassName="custom-backdrop"
                   keyboard={false}
                   aria-labelledby="contained-modal-title-vcenter"
                   centered
                   dialogClassName="custom-modal"
            >
                <Modal.Header className="border-0">
                    <Modal.Title className="text-center w-100 fw-bold">Registra un bambino</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <InformazioniBambinoValidatedForm
                        handleSubmit={handleSubmit}
                        formRef={formRef}
                    ></InformazioniBambinoValidatedForm>
                </Modal.Body>

                <Modal.Footer className="border-0 d-flex justify-content-end">
                    <Button variant="btn-outline-secondary btn-annulla" onClick={handleClose}>Annulla</Button>
                    <Button variant="btn-outline-primary btn-conferma" onClick={() => formRef.current.submitForm()}>Conferma</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default RegisterBambino;
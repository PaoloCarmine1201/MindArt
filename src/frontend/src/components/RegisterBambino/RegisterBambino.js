import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useState} from "react";
import RegisterBambinoValidatedForm from "./RegisterBambinoValidatedForm";

function RegisterBambino(){

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    function handleSubmit(values) {
        console.log('Submitted values:', values);
        setShow(false);
    }

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
                    <RegisterBambinoValidatedForm handleSubmit={handleSubmit}></RegisterBambinoValidatedForm>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Annulla</Button>
                    <Button variant="primary" type="submit" form="registerBambinoForm">Conferma</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default RegisterBambino;
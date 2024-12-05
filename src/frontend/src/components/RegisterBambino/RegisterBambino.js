import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useState} from "react";
import {Form} from "react-bootstrap";

function RegisterBambino(){

    const [show, setShow] = useState(false);
    const [inputs, setInputs] = useState({});

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setInputs(values => ({...values, [name]: value}))
    }

    function handleSubmit(){
        //TODO: implementare la chiamata al backend
        alert(inputs);
    }

    return (
        <>
            <Button variant="primary" onClick={handleShow}>Aggiungi Bambino</Button>
            <Modal show={show}
                   onHide={handleClose}
                   backdrop="static"
                   keyboard={false}
                   size="lg"
                   aria-labelledby="contained-modal-title-vcenter"
                   centered
            >
                <Modal.Header >
                    <Modal.Title>Registra un bambino</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="registerBambino.nome">
                            <Form.Label>Nome:</Form.Label>
                            <Form.Control type="text" placeholder="Es. peppino"/>
                        </Form.Group>

                        <Form.Group controlId="registerBambino.cognome">
                            <Form.Label>Cognome:</Form.Label>
                            <Form.Control type="text" placeholder="Es. rossi"/>
                        </Form.Group>
                        <Form.Group controlId="registerBambino.sesso">
                            <Form.Label>Sesso:</Form.Label>
                            <Form.Select aria-label="Sesso:">
                                <option value="maschio">Maschio</option>
                                <option value="femmina">Femmina</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group controlId="registerBambino.dataDiNascita">
                            <Form.Label>Data di nascita:</Form.Label>
                            <Form.Control type="date" />
                        </Form.Group>

                        <Form.Group controlId="registerBambino.codiceFiscale">
                            <Form.Label>Codice fiscale:</Form.Label>
                            <Form.Control type="text" />
                        </Form.Group>

                        <Form.Group controlId="registerBambino.emailGenitore">
                            <Form.Label>Email genitore:</Form.Label>
                            <Form.Control type="email" />
                        </Form.Group>

                        <Form.Group controlId="registerBambino.telefonoGenitore">
                            <Form.Label>Numero telefono genitore:</Form.Label>
                            <Form.Control type="text" />
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Annulla</Button>
                    <Button variant="primary" onClick={handleSubmit}>Conferma</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default RegisterBambino;
import React from "react";
import "../../style/Modal.css"
import { Modal, Button, Form } from "react-bootstrap";

const ValutazionePopup = ({ show, onClose, onSubmit, valutazione, setValutazione }) => {
    return (
        <Modal show={show}
               backdropClassName="custom-backdrop"
               keyboard={false}
               aria-labelledby="contained-modal-title-vcenter"
               dialogClassName="custom-modal"

               onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Valuta Disegno</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="formValutazione">
                        <label>Valutazione (1-10)</label>
                        <Form.Control
                            type="number"
                            min="1"
                            max="10"
                            value={valutazione}
                            onChange={(e) => setValutazione(e.target.value)}
                            placeholder="Inserisci un voto da 1 a 10"
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button
                    variant="secondary"
                    className="btn-cancella"
                    onClick={onClose}>

                    Annulla
                </Button>
                <Button
                    variant="primary"
                    className="btn-conferma"
                    onClick={onSubmit}>

                    Invia
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ValutazionePopup;

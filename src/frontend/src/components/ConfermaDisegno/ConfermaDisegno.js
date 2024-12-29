import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import axiosInstance from "../../config/axiosInstance";
import {Button, Modal} from "react-bootstrap";
import "../../style/Modal.css"

const ConfermaDisegno = () => {
    const [showModal, setShowModal] = useState(false);
    const [idSessione, setIdSessione] = useState(1); // todo id dinamico

    const handleSubmit = async () => {
        try {
            const response = await axiosInstance().patch(`/api/bambino/sessione/${idSessione}/consegna`);
            if (response && response.status === 200) {
                console.log("Disegno consegnato con successo.");
            }
        } catch (error) {
            if (error.response && error.response.status === 404) {
                console.log("ID della sessione non trovato.");
            } else if (error.response && error.response.status === 403){
                console.log("Errore nella consegna del disegno.");
            } else {
                console.error("Errore: " + error);
            }
        } finally {
            setShowModal(false);
        }
    };

    const handleOpenModal = () => {
        setShowModal(true);
    };

    return (
        <div className="container text-center mt-5">
            <Button className="btn-conferma" onClick={handleOpenModal}>
                Consegna Disegno
            </Button>

            {/* Modale di conferma */}
            <Modal
                show={showModal}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <Modal.Header>
                    <Modal.Title>
                        Sei sicuro di voler consegnare?
                    </Modal.Title>
                </Modal.Header>
                <Modal.Footer>
                    <Button
                        className="btn-cancella"
                        onClick={() => {
                            setShowModal(false)
                        }}
                    >
                        Annulla
                    </Button>
                    <Button
                        className="btn-conferma"
                        onClick={handleSubmit}
                    >
                        Conferma
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default ConfermaDisegno;
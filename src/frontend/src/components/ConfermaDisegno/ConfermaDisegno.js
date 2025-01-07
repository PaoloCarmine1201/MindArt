import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import axiosInstance from "../../config/axiosInstance";
import {Button, Modal} from "react-bootstrap";
import "../../style/Modal.css"

const ConfermaDisegno = ({nomeBottone = "Consegna"}) => {
    const [showModal, setShowModal] = useState(false);

    const handleSubmit = async () => {
        try {
            const response = await axiosInstance.post(`/api/bambino/sessione/consegna`,{});
            if (response && response.status === 200) {
                console.log("Disegno consegnato con successo.");
                window.location.href = "/childlogin";
                localStorage.removeItem("jwtToken");
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
        <div className="">
            <Button className="btn-conferma consegna" onClick={handleOpenModal}>
                {nomeBottone}
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
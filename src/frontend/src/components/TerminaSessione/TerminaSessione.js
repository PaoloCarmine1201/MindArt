import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import axiosInstance from "../../config/axiosInstance";
import { Button, Modal } from "react-bootstrap";
import "../../style/Modal.css";
import { useNavigate } from "react-router-dom";
import { toast } from 'react-toastify';


const TerminaSessione = ({ onSessionClosed }) => {
    const [showModal, setShowModal] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async () => {

        axiosInstance.post(`/api/terapeuta/sessione/termina`, {})
            .then((response) => {
                if (response && response.status === 200) {
                    navigate("/home")
                    onSessionClosed();
                    toast.success("Sessione terminata!");
                }
            })
            .catch((error) => {
                toast.error("C'è stato un errore :(");
                if (error.response && error.response.status === 404) {
                    console.log("ID della sessione non trovato.");
                } else if (error.response && error.response.status === 403) {
                    console.log("Errore nella terminazione della sessione.");
                } else {
                    console.error("Errore: ", error);
                }
            })
            .finally(() => {
                setShowModal(false);
            });
    };

    const handleOpenModal = () => {
        setShowModal(true);
    };

    return (
        <div className="container text-center">
            <Button className="btn-conferma" onClick={handleOpenModal}>
                Termina sessione
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
                        Sei sicuro di voler terminare la sessione?
                    </Modal.Title>
                </Modal.Header>
                <Modal.Footer>
                    <Button
                        className="btn-cancella"
                        onClick={() => setShowModal(false)}
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

export default TerminaSessione;

import { useState, useRef, useEffect } from "react";
import Button from "react-bootstrap/Button";
import AvviaSessioneMultiStepModal from "./AvviaSessioneMultiStepModal";
import { toast } from 'react-toastify'; // Importa solo il metodo toast

function AvviaSessioneButton({ onSessionCreated, sessioneAttiva }) {
    const [showModal, setShowModal] = useState(false);
    const target = useRef(null);

    const handleShowModal = () => {
        if (!sessioneAttiva) {
            setShowModal(true);
        }
    };


    const handleCloseModal = () => setShowModal(false);

    const handleMouseEnter = () => {
        if (!sessioneAttiva) {
            toast.warning("Avviando la sessione non potrai crearne di altre finché non termini quella corrente.");
        }
    };

    return (
        <>
            <Button
                ref={target}
                onClick={handleShowModal}
                onMouseEnter={handleMouseEnter}
                className={sessioneAttiva ? "btn-annulla" : "btn-conferma"}
            >
                Avvia sessione
            </Button>
            <AvviaSessioneMultiStepModal show={showModal} onHide={handleCloseModal} onSessionCreated={onSessionCreated} />
        </>
    );
}

export default AvviaSessioneButton;

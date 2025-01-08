import { useState, useRef } from "react";
import Button from "react-bootstrap/Button";
import AvviaSessioneMultiStepModal from "./AvviaSessioneMultiStepModal";
import { toast } from 'react-toastify'; // Importa solo il metodo toast

function AvviaSessioneButton({ onSessionCreated, sessioneAttiva }) {
    const [showModal, setShowModal] = useState(false);
    const target = useRef(null);

    const handleCloseModal = () => setShowModal(false);

    const handleClick = () => {
        if (!sessioneAttiva) {
            toast.warning("Avviando la sessione non potrai crearne di altre finché non termini quella corrente.");
            setShowModal(true);
        }

    };

    return (
        <>
            <Button
                ref={target}
                onClick={handleClick}
                className={sessioneAttiva ? "btn-annulla" : "btn-conferma"}
            >
                Avvia sessione
            </Button>
            <AvviaSessioneMultiStepModal show={showModal} onHide={handleCloseModal} onSessionCreated={onSessionCreated} />
        </>
    );
}

export default AvviaSessioneButton;

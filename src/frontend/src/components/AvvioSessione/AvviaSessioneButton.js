import { useState, useRef } from "react";
import Button from "react-bootstrap/Button";
import Overlay from "react-bootstrap/Overlay";
import Tooltip from "react-bootstrap/Tooltip";
import AvviaSessioneMultiStepModal from "./AvviaSessioneMultiStepModal";

function AvviaSessioneButton({ sessioneAttiva }) {
    const [showModal, setShowModal] = useState(false);
    const [showTooltip, setShowTooltip] = useState(false);
    const target = useRef(null);

    const handleShowModal = () => {
        if (!sessioneAttiva) {
            setShowModal(true);
        }
    };

    const handleCloseModal = () => setShowModal(false);

    const handleMouseEnter = () => {
        if (sessioneAttiva) {
            setShowTooltip(true);
        }
    };

    const handleMouseLeave = () => setShowTooltip(false);

    return (
        <>
            <Button
                ref={target}
                onClick={handleShowModal}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
                className={sessioneAttiva ? "btn-secondary" : "btn-primary"}
            >
                Avvia sessione
            </Button>
            <Overlay target={target.current} show={showTooltip} placement="top">
                {(props) => (
                    <Tooltip id="button-tooltip" {...props}>
                        C'è una sessione già attiva
                    </Tooltip>
                )}
            </Overlay>
            <AvviaSessioneMultiStepModal show={showModal} onHide={handleCloseModal} />
        </>
    );
}

export default AvviaSessioneButton;

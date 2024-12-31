import {useState} from "react";
import Button from "react-bootstrap/Button";
import AvviaSessioneMultiStepModal from "./AvviaSessioneMultiStepModal";

function AvviaSessioneButton( {onSessionCreated } ) {
    const [show, setShow] = useState(false);

    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);

    return (
        <>
            <Button className="btn-conferma" onClick={handleShow}>
                Avvia sessione
            </Button>
            <AvviaSessioneMultiStepModal show={show} onHide={handleClose} onSessionCreated={onSessionCreated} />
        </>
    )
}
export default AvviaSessioneButton;
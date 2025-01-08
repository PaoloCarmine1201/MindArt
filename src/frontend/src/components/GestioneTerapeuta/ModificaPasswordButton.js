import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import ModificaPasswordModal from "./ModificaPasswordModal";

function ModificaPasswordButton () {
    const [showModal, setShowModal] = useState(false);
    const handleCloseModal = () => setShowModal(false);

    const handleClick = () => {
        setShowModal(true);
    };


    return (
        <>
            <Button
                className="btn-all"
                onClick={handleClick}>
                Modifica Password
            </Button>
            <ModificaPasswordModal show={showModal} onHide={handleCloseModal} />
        </>
    )

}
export default ModificaPasswordButton;
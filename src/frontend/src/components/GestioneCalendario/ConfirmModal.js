import {Button, Modal} from "react-bootstrap";

function ConfirmModal({showConfirmModal, handleDelete, setShowConfirmModal, onClose}) {

    return (
        <Modal show={showConfirmModal} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Sei sicuro di voler eliminare l'evento?</Modal.Title>
            </Modal.Header>
            <Modal.Footer>
                <Button variant="danger" onClick={() => { handleDelete(); setShowConfirmModal(false); }} className="custom-delete-btn">
                    Elimina
                </Button>
                <Button variant="secondary" onClick={onClose} className="custom-cancel-btn">
                    Annulla
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ConfirmModal;
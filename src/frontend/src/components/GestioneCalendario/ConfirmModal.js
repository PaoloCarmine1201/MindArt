import {Button, Modal} from "react-bootstrap";

function ConfirmModal({showConfirmModal, handleDelete, setShowConfirmModal, onClose}) {

    return (
        <Modal show={showConfirmModal} onHide={onClose}
               backdropClassName="custom-backdrop"
               keyboard={false}
               aria-labelledby="contained-modal-title-vcenter"
               centered
               dialogClassName="custom-modal">
            <Modal.Header closeButton>
                <Modal.Title>Sei sicuro di voler eliminare l'evento?</Modal.Title>
            </Modal.Header>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose} className="btn-annulla btn-outline-primary">
                    Annulla
                </Button>
                <Button variant="danger" onClick={() => { handleDelete(); setShowConfirmModal(false); }} className="btn-cancella btn-outline-primary">
                    Elimina
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ConfirmModal;
import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import '../style/EventModal.css';

function MyModal({ event, onSave, onDelete, onClose }) {
    const [title, setTitle] = useState(event.title || '');
    const [start, setStart] = useState(event.start ? formatDateTime(event.start) : formatDateTime(new Date()));
    const [end, setEnd] = useState(event.end ? formatDateTime(event.end) : formatDateTime(new Date()));

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave({
            ...event,
            title,
            start: new Date(start),
            end: new Date(end),
        });
    };

    const handleDelete = () => {
        if (event.id) {
            onDelete(event.id);
        }
    };

    return (
        <Modal show onHide={onClose} centered>
            <Form onSubmit={handleSubmit}>
                <Modal.Header closeButton>
                    <Modal.Title className="modal-title">
                        {event.id ? 'Modifica Evento' : 'Crea Nuovo Evento'}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3">
                        <Form.Label className="custom-label">Titolo</Form.Label>
                        <Form.Control
                            type="text"
                            value={title}
                            onChange={e => setTitle(e.target.value)}
                            required
                            placeholder="Inserisci il titolo dell'evento"
                            className="custom-control"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label className="custom-label">Inizio</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={start}
                            onChange={e => setStart(e.target.value)}
                            required
                            className="custom-control"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label className="custom-label">Fine</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={end}
                            onChange={e => setEnd(e.target.value)}
                            required
                            className="custom-control"
                        />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    {event.id && (
                        <Button variant="danger" onClick={handleDelete} className="custom-delete-btn">
                            Elimina
                        </Button>
                    )}
                    <Button variant="secondary" onClick={onClose} className="custom-cancel-btn">
                        Annulla
                    </Button>
                    <Button variant="primary" type="submit" className="custom-save-btn">
                        Salva
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
}

// Funzione per formattare la data in un valore compatibile con type="datetime-local"
function formatDateTime(date) {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth()+1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const mins = String(d.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${mins}`;
}

export default MyModal;

// src/components/EventoForm.jsx

import React, {useState} from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { Formik, Field, Form as FormikForm, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import '../../style/EventModal.css';
import ConfirmModal from "./ConfirmModal";

// Validation schema using Yup
const EventoSchema = (existingEvents, currentEventId) => Yup.object().shape({
    title: Yup.string()
        .min(3, 'Il titolo deve essere lungo almeno 3 caratteri')
        .max(100, 'Il titolo non può superare i 100 caratteri')
        .required('Il titolo è obbligatorio'),
    start: Yup.date()
        .required('La data e l\'ora di inizio sono obbligatorie'),
    end: Yup.date()
        .min(Yup.ref('start'), 'La data di fine deve essere successiva all\'inizio')
        .required('La data e l\'ora di fine sono obbligatorie')
        .test('no-overlap', 'L\'evento si sovrappone con un altro evento esistente', function(value) {
            const { start } = this.parent;
            if (!start || !value) return true; // Handled by other validations
            const newStart = new Date(start);
            const newEnd = new Date(value);

            const isOverlap = existingEvents.some(ev => {
                if (currentEventId && ev.id === currentEventId) {
                    // Skip the current event when editing
                    return false;
                }
                const evStart = new Date(ev.start);
                const evEnd = new Date(ev.end);
                return (newStart < evEnd) && (evStart < newEnd);
            });

            return !isOverlap;
        }),
});
function EventoForm({ event, onSave, onDelete, onClose, existingEvents}) {

    const [showConfirmModal, setShowConfirmModal] = useState(false);

    const initialValues = {
        title: event.title || '',
        start: event.start ? formatDateTime(event.start) : formatDateTime(new Date()),
        end: event.end ? formatDateTime(event.end) : formatDateTime(new Date()),
    };

    const handleSubmit = (values, { setSubmitting }) => {
        const eventData = {
            ...event,
            title: values.title,
            start: new Date(values.start),
            end: new Date(values.end),
        };
        onSave(eventData);
        setSubmitting(false);
    };

    const handleDelete = () => {
        if (event.id) {
            onDelete(event.id);
        }
    };

    return (
        <>
            <Modal show onHide={onClose} centered className={`app-container ${showConfirmModal ? 'blur' : ''}`}>
                <Formik
                    initialValues={initialValues}
                    validationSchema={EventoSchema(existingEvents,event.id)}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting }) => (
                        <FormikForm>
                            <Modal.Header closeButton>
                                <Modal.Title className="modal-title">
                                    {event.id ? 'Modifica Evento' : 'Crea Nuovo Evento'}
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Form.Group className="mb-3">
                                    <Form.Label className="custom-label">Titolo</Form.Label>
                                    <Field
                                        type="text"
                                        name="title"
                                        placeholder="Inserisci il titolo dell'evento"
                                        className="form-control custom-control"
                                    />
                                    <div className="text-danger">
                                        <ErrorMessage name="title" />
                                    </div>
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label className="custom-label">Inizio</Form.Label>
                                    <Field
                                        type="datetime-local"
                                        name="start"
                                        className="form-control custom-control"
                                    />
                                    <div className="text-danger">
                                        <ErrorMessage name="start" />
                                    </div>
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label className="custom-label">Fine</Form.Label>
                                    <Field
                                        type="datetime-local"
                                        name="end"
                                        className="form-control custom-control"
                                    />
                                    <div className="text-danger">
                                        <ErrorMessage name="end" />
                                    </div>
                                </Form.Group>
                            </Modal.Body>
                            <Modal.Footer>
                                {event.id && (
                                    <Button variant="danger" onClick={() => {setShowConfirmModal(true);}} className="custom-delete-btn">
                                        Elimina
                                    </Button>
                                )}
                                <Button variant="secondary" onClick={onClose} className="custom-cancel-btn">
                                    Annulla
                                </Button>
                                <Button variant="primary" type="submit" disabled={isSubmitting} className="custom-save-btn">
                                    Salva
                                </Button>
                            </Modal.Footer>
                        </FormikForm>
                    )}
                </Formik>
            </Modal>
            <ConfirmModal handleDelete={handleDelete} setShowConfirmModal={setShowConfirmModal} showConfirmModal={showConfirmModal} onClose={onClose}>
            </ConfirmModal>
        </>
    );
}

// Funzione per formattare la data in un valore compatibile con type="datetime-local"
function formatDateTime(date) {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const mins = String(d.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${mins}`;
}

export default EventoForm;

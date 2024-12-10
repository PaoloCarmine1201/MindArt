// src/components/EventoForm.jsx

import React, {useState} from 'react';
import {Modal, Button, Form, FloatingLabel} from 'react-bootstrap';
import { Formik, Field, Form as FormikForm, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import '../../style/RegisterBambino.css';
import '../../style/Button.css';

import ConfirmModal from "./ConfirmModal";


// Validation schema using Yup
const EventoSchema = () => Yup.object().shape({
    title: Yup.string()
        .min(3, 'Il titolo deve essere lungo almeno 3 caratteri')
        .max(100, 'Il titolo non può superare i 100 caratteri')
        .required('Il titolo è obbligatorio'),
    start: Yup.date()
        .required('La data e l\'ora di inizio sono obbligatorie'),
    end: Yup.date()
        .min(Yup.ref('start'), 'La data di fine deve essere successiva all\'inizio')
        .required('La data e l\'ora di fine sono obbligatorie'),
});

function EventoForm({ event, onSave, onDelete, onClose}) {

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
                    validationSchema={EventoSchema(event.id)}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting , errors, touched}) => (
                        <FormikForm>
                            <Modal.Header closeButton>
                                <Modal.Title className="modal-title">
                                    {event.id ? 'Modifica Evento' : 'Crea Nuovo Evento'}
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Form.Group controlId="eventoForm.title" className="form-floating">
                                    <FloatingLabel label="Titolo">
                                        <Field
                                            name="title"
                                            type="text"
                                            placeholder="Inserisci il titolo dell'evento"
                                            as={Form.Control}
                                            isInvalid={!!errors.title && touched.title}
                                        />
                                        <Form.Control.Feedback type="invalid" className="tooltip">
                                            <ErrorMessage name="title" />
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>
                                <Form.Group controlId="eventoForm.start" className="form-floating">
                                    <FloatingLabel label="Inizio">
                                        <Field
                                            name="start"
                                            type="datetime-local"
                                            as={Form.Control}
                                            isInvalid={!!errors.start && touched.start}
                                        />
                                        <Form.Control.Feedback type="invalid" className="tooltip">
                                            <ErrorMessage name="start" />
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>
                                <Form.Group controlId="eventoForm.end" className="form-floating">
                                    <FloatingLabel label="Fine">
                                        <Field
                                            name="end"
                                            type="datetime-local"
                                            as={Form.Control}
                                            isInvalid={!!errors.end && touched.end}
                                        />
                                        <Form.Control.Feedback type="invalid" className="tooltip">
                                            <ErrorMessage name="end" />
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>
                            </Modal.Body>
                            <Modal.Footer>
                                {event.id && (
                                    <Button
                                        onClick={() => setShowConfirmModal(true)}
                                        className="btn-cancella btn-outline-primary"
                                    >
                                        Elimina
                                    </Button>
                                )}
                                <Button variant="secondary" onClick={onClose} className="btn-annulla btn-outline-primary">
                                    Annulla
                                </Button>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    disabled={isSubmitting}
                                    className="btn-conferma btn-outline-primary"
                                >
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

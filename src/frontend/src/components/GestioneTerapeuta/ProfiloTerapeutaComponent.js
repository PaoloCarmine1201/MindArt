import { useEffect, useState } from "react";
import axiosInstance from "../../config/axiosInstance";
import '../../style/ProfiloTerapeutaStyle.css';
import '../../style/Modal.css';
import { Link } from "react-router-dom";
import Modal from "react-bootstrap/Modal";
import { ModalBody, ModalFooter, Form, FloatingLabel } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { toast } from "react-toastify";
import { Formik } from "formik";
import * as yup from "yup";
import {useAuth} from "../../auth/AuthProvider";

function ProfiloTerapuetaComponent() {
    const [terapeuta, setTerapeuta] = useState(null);
    const [showModal, setShowModal] = useState(false);

    const { logout } = useAuth();
    
    const validationSchema = yup.object().shape({
        nome: yup
            .string()
            .matches(/^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]{2,50}$/, 'formato nome non valido')
            .required("Il nome è obbligatorio"),
        cognome: yup
            .string()
            .matches(/^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]{2,50}$/, 'formato cognome non valido')
            .required("Il cognome è obbligatorio"),
        email: yup
            .string()
            .email("Email non valida")
            .required("L'email è obbligatoria"),
    });

    useEffect(() => {
        axiosInstance.get(`api/terapeuta/get`)
            .then(response => {
                setTerapeuta(response.data);
            })
            .catch(error => {
                console.error("Errore durante il recupero dei dati del terapeuta:", error);
            });
    }, []);

    const handleClick = () => {
        setShowModal(true);
    }

    const handleSubmit = (values) => {
        axiosInstance.post(`api/terapeuta/update`, values)
            .then(response => {
                setShowModal(false);

                if (values.email !== terapeuta.email) {
                    toast.info("Sarai reindirizzato al login.",
                        {
                            autoClose: 2000
                        }
                    );
                    setTimeout(() => {
                        // Reindirizza al login
                        logout()
                        window.location.href = "/login";
                    }, 2000);
                }
                else{
                    toast.success('Modifiche avvenute con successo.',
                        {
                            autoClose: 1000
                        }
                    );
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                }

            })
            .catch(error => {
                console.error("Errore durante il salvataggio delle modifiche:", error);
                toast.error("Errore durante il salvataggio delle modifiche.");
            });
    };


    if (!terapeuta) {
        return <p>Caricamento in corso...</p>;
    }

    return (
        <div className="dettaglio-container">
            <Modal
                show={showModal}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <Modal.Header className="border-0">
                    <Modal.Title className="text-center w-100 fw-bold">Modifica i tuoi dati</Modal.Title>
                </Modal.Header>
                <ModalBody>
                    {/* Form per modificare i dati del terapeuta */}
                    <Formik
                        initialValues={{
                            nome: terapeuta.nome,
                            cognome: terapeuta.cognome,
                            email: terapeuta.email,
                            dataDiNascita: terapeuta.dataDiNascita
                        }}
                        validationSchema={validationSchema}
                        onSubmit={handleSubmit}
                    >
                        {({ handleSubmit, handleChange, handleBlur, values, errors, touched }) => (
                            <Form id="formTerapeuta" noValidate onSubmit={handleSubmit}>
                                <Form.Group controlId="formNome">
                                    <FloatingLabel controlId="formNome" label="Nome">
                                        <Form.Control
                                            name="nome"
                                            type="text"
                                            placeholder="Inserisci il nome"
                                            value={values.nome}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                            isInvalid={touched.nome && !!errors.nome}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.nome}
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>

                                <Form.Group controlId="formCognome">
                                    <FloatingLabel controlId="formCognome" label="Cognome">
                                        <Form.Control
                                            name="cognome"
                                            type="text"
                                            placeholder="Inserisci il cognome"
                                            value={values.cognome}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                            isInvalid={touched.cognome && !!errors.cognome}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.cognome}
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>


                                <Form.Group controlId="formEmail">
                                    <FloatingLabel controlId="formEmail" label="Email">
                                        <Form.Control
                                            name="email"
                                            type="email"
                                            placeholder="Inserisci l'email"
                                            value={values.email}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                            isInvalid={touched.email && !!errors.email}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.email}
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>

                                <ModalFooter>
                                    <Button
                                        onClick={() => setShowModal(false)}
                                        className="btn-cancella"
                                    >
                                        Annulla
                                    </Button>
                                    <Button
                                        type="submit"
                                        className="btn-conferma"
                                    >
                                        Salva
                                    </Button>
                                </ModalFooter>
                            </Form>
                        )}
                    </Formik>
                </ModalBody>
            </Modal>
            <h2 className="dettaglio-header">{terapeuta.nome} {terapeuta.cognome}</h2>
            <div className="dettaglio-section">
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Data di Nascita:</label>
                    <span className="dettaglio-value">
                        {new Date(terapeuta.dataDiNascita).toLocaleDateString('it-IT')}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Email:</label>
                    <span className="dettaglio-value">{terapeuta.email}</span>
                </div>
            </div>

            <div className="dettaglio-button-container text-end justify-content-end">
                {/* Pulsante Modifica */}
                <Button
                    onClick={handleClick}
                    className="btn-all"
                >
                    Modifica i tuoi dati
                </Button>
            </div>

            {/* Pulsante Indietro */}
            <Link to="/" className="dettaglio-button-link">
                <p className="dettaglio-back-button">↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default ProfiloTerapuetaComponent;

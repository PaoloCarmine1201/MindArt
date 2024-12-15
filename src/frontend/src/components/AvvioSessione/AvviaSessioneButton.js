import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { Formik } from 'formik';
import { stepOneSchema, stepTwoSchema, stepThreeSchema } from './SchemaValidazione';
import SelezioneTipo from "./SelezioneTipo";
import SelezioneMateriale from "./SelezioneMateriale";
import SelezioneBambino from "./SelezioneBambino";

const MultiStepModal = ({ show, onHide }) => {
    const [currentStep, setCurrentStep] = useState(1);

    const initialValues = {
        tipoSessione: 'attività',
        materiale: '',
        bambino: '',
    };

    // Funzione per ottenere lo schema in base allo step corrente
    const getValidationSchema = (step) => {
        switch (step) {
            case 1:
                return stepOneSchema;
            case 2:
                return stepTwoSchema;
            case 3:
                return stepThreeSchema;
            default:
                return stepOneSchema;
        }
    };

    const handleNext = async (validateForm, setTouched, errors) => {
        // Forziamo la validazione
        const errs = await validateForm();
        setTouched(
            Object.keys(errs).reduce((acc, key) => {
                acc[key] = true;
                return acc;
            }, {})
        );

        // Se non ci sono errori relativi allo step corrente, passiamo allo step successivo
        const currentErrors = Object.keys(errs).filter((key) => {
            // Qui potresti filtrare i campi di pertinenza dello step corrente,
            // per semplicità consideriamo qualsiasi errore come bloccante.
            return errs[key] !== undefined;
        });

        if (currentErrors.length === 0) {
            setCurrentStep((prev) => prev + 1);
        }
    };

    const handleBack = () => {
        setCurrentStep((prev) => prev - 1);
    };

    const handleSubmit = (values) => {
        console.log('Form values: ', values); //todo: invia i dati al server
        onHide();
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Formik
                initialValues={initialValues}
                validationSchema={getValidationSchema(currentStep)}
                onSubmit={handleSubmit}
                validateOnBlur={true}
                validateOnChange={true}
            >
                {({ handleSubmit, validateForm, errors, setTouched }) => (
                    <Form noValidate onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
                        <Modal.Header closeButton>
                            <Modal.Title>Avvia Sessione</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {currentStep === 1 && <SelezioneTipo/>}
                            {currentStep === 2 && <SelezioneMateriale/>}
                            {currentStep === 3 && <SelezioneBambino/>}
                        </Modal.Body>
                        <Modal.Footer>
                            {currentStep > 1 && (
                                <Button variant="secondary" onClick={handleBack}>
                                    Indietro
                                </Button>
                            )}
                            {currentStep < 3 && (
                                <Button variant="primary" onClick={() => handleNext(validateForm, setTouched, errors)}>
                                    Avanti
                                </Button>
                            )}
                            {currentStep === 3 && (
                                <Button variant="success" type="submit">
                                    Invia
                                </Button>
                            )}
                        </Modal.Footer>
                    </Form>
                )}
            </Formik>
        </Modal>
    );
};

export default MultiStepModal;

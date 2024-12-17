import React, {useState} from 'react';
import {Modal, Button, Form, FormLabel, FormGroup} from 'react-bootstrap';
import {Formik} from 'formik';
import {stepOneSchema, stepTwoSchema, stepThreeSchema} from './SchemaValidazione';
import SelezioneTipo from "./SelezioneTipo";
import SelezioneMateriale from "./SelezioneMateriale";
import SelezioneBambino from "./SelezioneBambino";

const AvviaSessioneMultiStepModal = ({show, onHide}) => {
    const [currentStep, setCurrentStep] = useState(1);

    const initialValues = {
        tipoSessione: '',
        materiale: '',
        bambino: '',
    };

    // Funzione per ottenere lo schema in base allo step corrente
    const getValidationSchema = () => {
        switch (currentStep) {
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

    const handleNext = async (validateForm, setTouched, values, errors) => {
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
            if(currentStep === 1 && !(values['tipoSessione'] === 'apprendimento')){
                setCurrentStep((prev) => prev + 2);
            }else {
                setCurrentStep((prev) => prev + 1);
            }
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
        <Formik
            initialValues={initialValues}
            validationSchema={getValidationSchema(currentStep)}
            onSubmit={handleSubmit}
            validateOnBlur={true}
            validateOnChange={true}
            validate={getValidationSchema()}
        >
            {({handleSubmit, validateForm, values, errors, touched, setTouched, resetForm}) => (
                <Modal show={show} onHide={() => {
                    resetForm(); // Resetta il form ai valori iniziali
                    setCurrentStep(1); // Se vuoi tornare anche allo step iniziale
                    onHide();
                }}>

                    <Form noValidate onSubmit={(e) => {
                        e.preventDefault();
                        handleSubmit();
                    }}>
                        <Modal.Header>
                            <Modal.Title>Avvia Sessione</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {currentStep === 1 && <FormGroup>
                                <FormLabel>Tipo sessione</FormLabel>
                                <Form.Check
                                    type="radio"
                                    name="tipoSessione"
                                    label="attività"
                                    value="attività"
                                    isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                                    onChange={handleChange}
                                >
                                </Form.Check>
                                <Form.Check
                                    type="radio"
                                    name="tipoSessione"
                                    label="apprendimento"
                                    value="apprendimento"
                                    isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                                    onChange={handleChange} >
                                </Form.Check>
                                <Form.Control.Feedback type="invalid">
                                    {errors.tipoSessione instanceof String ? errors.tipoSessione : ''}
                                </Form.Control.Feedback>
                            </FormGroup>}
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
                                <Button variant="primary" onClick={() => handleNext(validateForm, touched, setTouched, values, errors)}>
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
                </Modal>
            )}
        </Formik>
    );
};

export default AvviaSessioneMultiStepModal;

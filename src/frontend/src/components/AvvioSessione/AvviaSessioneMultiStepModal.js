import React, { useEffect, useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { Formik } from 'formik';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import axios from 'axios';
import {
    stepOneSchema,
    stepTwoSchema,
    stepThreeSchema
} from './SchemaValidazione';
import SelezioneTipo from './SelezioneTipo';
import SelezioneMateriale from './SelezioneMateriale';
import SelezioneBambino from './SelezioneBambino';

import '../../style/Button.css';
import '../../style/Modal.css';
import '../../style/Transition.css'; // Stile per le animazioni

const AvviaSessioneMultiStepModal = ({ show, onHide }) => {
    const [currentStep, setCurrentStep] = useState(1);
    const [childrenList, setChildrenList] = useState([]);
    const [loadingChildren, setLoadingChildren] = useState(false);
    const [childrenError, setChildrenError] = useState(null);
    const [direction, setDirection] = useState('forward'); // 'forward' o 'backward'

    const initialValues = {
        tipoSessione: '',
        materiale: '',
        bambino: [],
    };

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

    useEffect(() => {
        if (currentStep === 3) {
            setLoadingChildren(true);
            setChildrenError(null);

            axios.get('http://localhost:8080/api/bambino/getallbyterapeuta?terapeuta=1') /*TODO: Sostituire con l'ID del terapeuta loggato*/
                .then(response => {
                    setChildrenList(response.data);
                    setLoadingChildren(false);
                })
                .catch(error => {
                    setChildrenError('Errore nel caricamento dei bambini.');
                    setLoadingChildren(false);
                });
        }
    }, [currentStep]);

    const handleNext = async (validateForm, touched, setTouched, values, errors) => {

        const errs = await validateForm();
        setDirection('forward');
        setTouched(
            Object.keys(errs).reduce((acc, key) => {
                acc[key] = true;
                return acc;
            }, {})
        );

        const currentErrors = Object.keys(errs).filter(key => errs[key] !== undefined);

        if (currentErrors.length === 0) {
            if (currentStep === 1 && values.tipoSessione !== 'apprendimento') {
                setCurrentStep(prev => prev + 2);
            } else {
                setCurrentStep(prev => prev + 1);
            }
        }
    };

    const handleBack = () => {
        setDirection('backward');
        if(currentStep === 3 && initialValues.tipoSessione !== 'apprendimento') {
            setCurrentStep(prev => prev - 2);
        } else {
            setCurrentStep(prev => prev - 1);
        }
    };

    const handleSubmit = (values, { resetForm }) => {
        console.log('Form values: ', values);
        resetForm();
        setCurrentStep(1);
        onHide();
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return <SelezioneTipo />;
            case 2:
                return <SelezioneMateriale />;
            case 3:
                return (
                    <SelezioneBambino
                        childrenList={childrenList}
                        loading={loadingChildren}
                        error={childrenError}
                    />
                );
            default:
                return <SelezioneTipo />;
        }
    };

    return (
        <Formik
            initialValues={initialValues}
            validationSchema={getValidationSchema()}
            onSubmit={handleSubmit}
            validateOnBlur={true}
            validateOnChange={true}
        >
            {({
                  handleSubmit,
                  validateForm,
                  touched,
                  setTouched,
                  values,
                  errors,
                  resetForm
              }) => (
                <Modal
                    show={show}
                    dialogClassName='custom-modal tall-modal-dialog'
                    backdropClassName='custom-backdrop'
                    aria-labelledby='contained-modal-title-vcenter'
                    centered
                    onHide={() => {
                        resetForm();
                        setCurrentStep(1);
                        onHide();
                    }}>
                    <Form noValidate onSubmit={(e) => {
                        e.preventDefault();
                        handleSubmit();
                    }}>
                        <Modal.Header>
                            <Modal.Title className="w-100 text-center">Avvia Sessione</Modal.Title>
                        </Modal.Header>
                        <Modal.Body className="tall-modal-body" style={{ height: '350px', overflow: 'hidden' }}>
                            <TransitionGroup>
                                <CSSTransition
                                    key={currentStep}
                                    classNames={direction === 'forward' ? 'slide-forward' : 'slide-backward'}
                                    timeout={300}
                                >
                                    {renderStep()}
                                </CSSTransition>
                            </TransitionGroup>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="btn-outline-pimary btn-cancella" onClick={() => {
                                resetForm();
                                setCurrentStep(1);
                                onHide();
                            }}>
                                Annulla
                            </Button>
                            {currentStep > 1 && (
                                <Button variant="btn-outline-secondary btn-annulla" onClick={handleBack}>
                                    Indietro
                                </Button>
                            )}
                            {currentStep < 3 && (
                                <Button
                                    variant="btn-outline-primary btn-conferma"
                                    onClick={() => handleNext(validateForm, touched, setTouched, values, errors)}
                                >
                                    Avanti
                                </Button>
                            )}
                            {currentStep === 3 && (
                                <Button variant="btn-outline-pimary btn-conferma" type="submit">
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

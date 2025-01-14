import React, { useEffect, useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { Formik } from 'formik';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import {
    stepOneSchema,
    stepTwoSchema,
    stepFourSchema, stepThreeSchemaMult, stepThreeSchemaSingle
} from './SchemaValidazione';
import SelezioneTipo from './SelezioneTipo';
import SelezioneMateriale from './SelezioneMateriale';
import SelezioneBambino from './SelezioneBambino';

import '../../style/Button.css';
import '../../style/Modal.css';
import '../../style/Transition.css';
import axiosInstance from "../../config/axiosInstance";
import InserimentoAssegnazione from "./InserimentoAssegnazione"; // Stile per le animazioni
import { toast } from 'react-toastify';


const AvviaSessioneMultiStepModal = ({ show, onHide, onSessionCreated }) => {
    const [currentStep, setCurrentStep] = useState(1);

    const [materialList, setMaterialList] = useState([]);
    const [loadingMaterial, setLoadingMaterial] = useState(false);
    const [materialError, setMaterialError] = useState(null);

    const [childrenList, setChildrenList] = useState([]);
    const [loadingChildren, setLoadingChildren] = useState(false);
    const [childrenError, setChildrenError] = useState(null);

    const [singleSession, setSingleSession] = useState(false);
     // 'forward' o 'backward'
    const [direction, setDirection] = useState("forward");

    const initialValues = {
        tipoSessione: '',
        materiale: '',
        bambini: [],
        temaAssegnato: ''
    };

    const getValidationSchema = () => {
        switch (currentStep) {
            case 1:
                return stepOneSchema;
            case 2:
                return stepTwoSchema;
            case 3:
                return singleSession ?
                    stepThreeSchemaSingle : stepThreeSchemaMult;
            case 4:
                return stepFourSchema;
            default:
                return stepOneSchema;
        }
    };

    useEffect(() => {
        if(currentStep === 2){
            setLoadingMaterial(true);
            setMaterialError(null);

            axiosInstance.get('http://localhost:8080/api/terapeuta/materiale/')
                .then(response => {
                    setMaterialList(response.data);
                    setLoadingMaterial(false);
                })
                .catch(() => {
                    setMaterialError('Errore nel caricamento dei materiali.');
                    setLoadingMaterial(false);
                });
        }
        else if (currentStep === 3) {
            setLoadingChildren(true);
            setChildrenError('');

            axiosInstance.get('http://localhost:8080/api/terapeuta/bambino/getallbyterapeuta')
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

    const handleNext = async (validateForm, touched, setTouched, values) => {

        const errs = await validateForm()

        setTouched(
            Object.keys(errs).reduce((acc, key) => {
                acc[key] = true;
                return acc;
            }, {})
        );

        const currentErrors = Object.keys(errs).filter(key => errs[key] !== undefined);

        if(values.tipoSessione === 'COLORE'){
            setSingleSession(true);
        }else{
            setSingleSession(false);
        }
        if (currentErrors.length === 0) {
            setDirection('forward');
            if (currentStep === 1 && values.tipoSessione === 'DISEGNO'){
                setCurrentStep(prev => prev + 2);
            } else {
                setCurrentStep(prev => prev + 1);
            }
        } else {
            toast.error('Compila correttamente i campi obbligatori.');
        }
    };

    const handleBack = (values) => {
        setDirection('backward');
        if(currentStep === 3 && values.tipoSessione === 'DISEGNO') {
            setCurrentStep(prev => prev - 2);
        } else {
            setCurrentStep(prev => prev - 1);
        }
    };

    const handleSubmit = (values, { resetForm }) => {
        axiosInstance.post('/api/terapeuta/sessione/create', values)
            .then(r => {
                toast.success("Sessione creata con successo!");
                setTimeout(() => {
                    onSessionCreated(); // Chiama il callback dopo un breve ritardo
                }, 300);
            })
            .catch(r => console.log(r));

        resetForm();
        setCurrentStep(1);
        onHide();
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return <SelezioneTipo />;
            case 2:
                return <SelezioneMateriale
                        materialList={materialList}
                        setMaterialList={setMaterialList}
                        loading={loadingMaterial}
                        error={materialError}
                />;
            case 3:
                return (
                    <SelezioneBambino
                        childrenList={childrenList}
                        loading={loadingChildren}
                        errorState={childrenError}
                    />
                );
            case 4:
                return <InserimentoAssegnazione />;
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
                        setDirection('backward');
                        onHide();
                    }}>
                    <Form noValidate onSubmit={(e) => {
                        e.preventDefault();
                        handleSubmit();
                    }}>
                        <Modal.Header>
                            <Modal.Title className="w-100 text-center">Avvia Sessione</Modal.Title>
                        </Modal.Header>
                        <Modal.Body className="tall-modal-body" style={{paddingLeft: '40px', paddingRight: '40px', height: '350px', overflow: 'hidden',  }}>
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
                                setDirection('backward');
                                onHide();
                            }}>
                                Annulla
                            </Button>
                            {currentStep > 1 && (
                                <Button variant="btn-outline-secondary btn-annulla" onClick={() => handleBack(values)}>
                                    Indietro
                                </Button>
                            )}
                            {((currentStep < 3) || (currentStep === 3 && values.tipoSessione === 'DISEGNO')) && (
                                <Button
                                    variant="btn-outline-primary btn-conferma"
                                    onClick={() => handleNext(validateForm, touched, setTouched, values, errors)}
                                >
                                    Avanti
                                </Button>
                            )}
                            {((currentStep === 3 && values.tipoSessione !== 'DISEGNO') || (currentStep === 4 && values.tipoSessione === 'DISEGNO')) && (
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

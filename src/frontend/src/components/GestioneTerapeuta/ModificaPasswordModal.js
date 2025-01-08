import {useState} from "react";
import axiosInstance from "../../config/axiosInstance";
import Modal from "react-bootstrap/Modal";
import '../../style/Modal.css'
import {ModalBody, ModalFooter, Form, FloatingLabel, } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {toast} from "react-toastify";
import { Formik } from "formik";
import * as yup from "yup";
import {useAuth} from "../../auth/AuthProvider";

function ModificaPasswordModal({show, onHide}) {
    const validationSchema = yup.object().shape({
        oldPassword: yup
            .string()
            .required("Vecchia password richiesta"),
        newPassword: yup
            .string()
            .required("Nuova password richiesta")

    });

    const [passwordCriteria, setPasswordCriteria] = useState({
        length: false,
        uppercase: false,
        number: false,
        specialChar: false,
    });

    const { logout } = useAuth();



    // useEffect(() => {
    //     axiosInstance.get('api/terapeuta/get')
    //         .then(response => {
    //             setTerapeuta(response.data);
    //         })
    //         .catch(error => {
    //             console.error("Errore durante il recupero dei dati del terapeuta:", error);
    //         });
    // }, []);

    const handlePasswordChange = (newPassword) => {
        setPasswordCriteria({
            length: newPassword.length >= 8,
            uppercase: /[A-Z]/.test(newPassword),
            number: /[0-9]/.test(newPassword),
            specialChar: /[!?_.,:;@#$%^&*]/.test(newPassword),
        });
    };

    const handleSubmit = async (values) => {
        const {oldPassword, newPassword} = values;

        if (!oldPassword || !newPassword){
            toast.error("Compila tutti i campi.");
            return;
        }

        if (newPassword === oldPassword){
            toast.error("La nuova password deve essere diversa dalla vecchia.");
            return;
        }

        try {
            const response = await axiosInstance.post('api/terapeuta/cambia-password', {
                oldPassword: oldPassword,
                newPassword: newPassword,
            });

            if (response && response.status === 200) {
                toast.success("Password modificata con successo, sarai indirizzato al login.",
                    {
                        autoClose: 2000
                    }
                );
                setTimeout(() => {
                    logout();
                    window.location.href = "/login";
                }, 2000);

            } else {
                toast.error("Errore durante il cambio della password.");
            }
        } catch (error) {
            console.error("Errore durante il cambio della password.");

            if (error.response) {
                if (error.response.status === 400) {
                    toast.error("Vecchia password errata.");
                } else {
                    toast.error("Errore durante il cambio della password.");
                }
            }else{
                toast.error("Errore durante il cambio della password.");
            }
        }
    };

    return (
        <>

            {/* Modal di modifica */}
            <Modal
                show={show}
                onHide={onHide}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <Modal.Header className="border-0">
                    <Modal.Title className="text-center w-100 fw-bold">Modifica Password</Modal.Title>
                </Modal.Header>
                <ModalBody>
                    <Formik
                        initialValues={{
                            oldPassword: '',
                            newPassword: '',
                        }}
                        validationSchema={validationSchema}
                        onSubmit={(values) => handleSubmit(values)}>

                        {({ handleSubmit, handleChange, handleBlur, values, errors, touched }) => (
                            <Form id="oldPassword" noValidate onSubmit={handleSubmit}>
                                <Form.Group controlId="formOldPassword">
                                    <FloatingLabel controlId="formOldPassword" label="Vecchia Password">
                                        <Form.Control
                                            name = "oldPassword"
                                            type = "password"
                                            placeholder= "Inserisci la vecchia password"
                                            value={values.oldPassword}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                            isInvalid={touched.oldPassword && !!errors.oldPassword}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.oldPassword}
                                        </Form.Control.Feedback>
                                    </FloatingLabel>
                                </Form.Group>

                                <Form.Group controlId="formNewPassword">
                                    <FloatingLabel controlId="formNewPassword" label="Nuova Password">
                                        <Form.Control
                                            name="newPassword"
                                            type="password"
                                            placeholder="Inserisci la nuova password"
                                            value={values.newPassword}
                                            onChange={(e) => {
                                                handlePasswordChange(e.target.value); // Aggiorna i criteri
                                                handleChange(e); // Aggiorna il valore di Formik
                                            }}
                                            onBlur={handleBlur}
                                            isInvalid={touched.newPassword && !!errors.newPassword}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.newPassword}
                                        </Form.Control.Feedback>
                                        <ul>
                                            <li className={passwordCriteria.length ? "text-success" : "text-danger"}>
                                                Almeno 8 caratteri
                                            </li>
                                            <li className={passwordCriteria.uppercase ? "text-success" : "text-danger"}>
                                                Almeno una lettera maiuscola
                                            </li>
                                            <li className={passwordCriteria.number ? "text-success" : "text-danger"}>
                                                Almeno un numero
                                            </li>
                                            <li className={passwordCriteria.specialChar ? "text-success" : "text-danger"}>
                                                Almeno un carattere speciale (!?_.,:;@#$%^&*)
                                            </li>
                                        </ul>

                                    </FloatingLabel>
                                </Form.Group>

                                <ModalFooter>
                                    <Button
                                        onClick={onHide}
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
        </>
    );
}

export default ModificaPasswordModal;

import React, { useState } from 'react';
import '../../style/Login.css';
import '../../style/Registration.css';
import {useNavigate} from "react-router-dom";
import {Button, Form, Stack} from "react-bootstrap";
import {toast} from "react-toastify";
import axiosInstance from "../../config/axiosInstance";

const Registration = () => {
    const [nome, setNome] = useState('');
    const [cognome, setCognome] = useState('');
    const [dataDiNascita, setdataDiNascita] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [fieldErrors, setFieldErrors] = useState({}); //check errore per ogni variabile
    const [passwordCriteria, setPasswordCriteria] = useState({
        length: false,
        uppercase: false,
        number: false,
        specialChar: false,
    });
    const navigate = useNavigate();

    const validateInputs = () => {
        let isValid = true;
        let errors = {};

        const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]{2,50}$/;
        // Validazione Nome
        if (!nome) {
            errors.nome = 'Inserisci il nome';
            isValid = false;
        } else if (!nomeRegex.test(nome)) {
            errors.nome = 'Formato nome non valido';
            isValid = false;
        }

        // Validazione Cognome
        if (!cognome) {
            errors.cognome = 'Inserisci il cognome';
            isValid = false;
        } else if (!nomeRegex.test(cognome)) {
            errors.cognome = 'Formato cognome non valido';
            isValid = false;
        }

        // Validazione Data di Nascita
        if (!dataDiNascita) {
            errors.dataDiNascita = 'Inserisci la data di nascita';
            isValid = false;
        } else if (new Date(dataDiNascita) >= new Date()) {
            errors.dataDiNascita = 'Data non valida';
            isValid = false;
        }

        // Validazione Email con regex
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!email) {
            errors.email = 'Inserisci l\'email';
            isValid = false;
        } else if (!emailRegex.test(email)) {
            errors.email = 'Formato email non valido';
            isValid = false;
        }

        // Validazione Password dinamica
        if (!password) {
            errors.password = 'Inserisci la password';
            isValid = false;
        } else if (!Object.values(passwordCriteria).every(Boolean)) {
            errors.password = 'La password deve soddisfare tutti i criteri.';
            isValid = false;
        }

        setFieldErrors(errors);
        return isValid;
    };

    const handlePasswordChange = (e) => {
        const value = e.target.value;
        setPassword(value);

        // Aggiornamento dinamico dei criteri della password
        setPasswordCriteria({
            length: value.length >= 8,
            uppercase: /[A-Z]/.test(value),
            number: /[0-9]/.test(value),
            specialChar: /[!?_.,:;@#$%^&*]/.test(value),
        });
    };

    const performRegistration = async () => {
        try {
            const payload = { nome, cognome, dataDiNascita, email, password };
            const response = await axiosInstance.post('/auth/terapeuta/register', payload);

            // Successo
            toast.success('Registrazione effettuata con successo!', {
                position: 'bottom-right'
            });
            window.location = '/login';
        } catch (err) {
            console.error('Registration error:', err);

            // Errori dal server
            if (err.response && err.response.data && err.response.data.errors) {
                const serverErrors = {};
                err.response.data.errors.forEach(errorItem => {
                    serverErrors[errorItem.field] = errorItem.message;
                });
                setFieldErrors(serverErrors);
            } else {
                // Errori generici
                toast.error('Si è verificato un errore durante la registrazione. Riprova più tardi.', {
                    position: 'bottom-right'
                });
            }
        }
    };

    const handleRegistration = async (e) => {
        e.preventDefault();
        if (validateInputs()) {
            await performRegistration();
        }
    };

    return (
        <Stack gap={4} className="login-container d-flex justify-content-center align-items-center vh-100" >
            <Form onSubmit={handleRegistration} className="card p-4 shadow-sm login-form">
                <h2 className="title">Registrazione</h2>

                <Form.FloatingLabel controlId="registration.name" label="Nome">
                    <Form.Control type="text" value={nome} onChange={e => setNome(e.target.value)}/>
                    {fieldErrors.nome && <p className="text-danger">{fieldErrors.nome}</p>}
                </Form.FloatingLabel>

                <Form.FloatingLabel controlId="registration.surname" label="Cognome">
                    <Form.Control type="text" value={cognome} onChange={e => setCognome(e.target.value)}/>
                    {fieldErrors.cognome && <p className="text-danger">{fieldErrors.cognome}</p>}
                </Form.FloatingLabel>

                <Form.FloatingLabel controlId="registration.dateOfBirth" label="Data Di Nascita">
                    <Form.Control type="date" value={dataDiNascita} onChange={e => setdataDiNascita(e.target.value)}/>
                    {fieldErrors.dataDiNascita && <p className="text-danger">{fieldErrors.dataDiNascita}</p>}
                </Form.FloatingLabel>

                <Form.FloatingLabel controlId="registration.email" label="Email">
                    <Form.Control type="text" value={email} onChange={e => setEmail(e.target.value)}/>
                    {fieldErrors.email && <p className="text-danger">{fieldErrors.email}</p>}
                </Form.FloatingLabel>

                <Form.FloatingLabel controlId="registration.password" label="Password">
                    <Form.Control
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                    />
                    {fieldErrors.password && <p className="text-danger">{fieldErrors.password}</p>}

                    {/* Feedback dinamico sui criteri */}
                    <ul className="password-criteria">
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
                </Form.FloatingLabel>

                <div className="d-flex justify-content-end gap-2">
                    <Button
                        variant="primary"
                        className="btn-disegno"
                        onClick={() => navigate("/login")}
                    >
                        Torna al Login
                    </Button>
                    <Button
                        variant="primary"
                        className="btn-conferma"
                        type="submit"
                    >
                        Registrati
                    </Button>
                </div>
            </Form>
        </Stack>
    );
};

export default Registration;

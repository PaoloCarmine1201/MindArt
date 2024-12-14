import React, { useState } from 'react';
import '../../style/Login.css';
import '../../style/Registration.css';
import {Link} from "react-router-dom";
import {Button, FloatingLabel, Form, Stack} from "react-bootstrap";
import axios from 'axios';

const Registration = () => {
    const [nome, setNome] = useState('');
    const [cognome, setCognome] = useState('');
    const [dataDiNascita, setdataDiNascita] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [terms, setTerms] = useState(''); //Termini e Condizioni
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState({}); //check errore per ogni variabile

    const validateInputs = () => {
        let isValid = true;
        let errors = {};

        // Validazione Nome
        if (!nome) {
            errors.nome = 'Inserisci il nome';
            isValid = false;
        }

        // Validazione Cognome
        if (!cognome) {
            errors.cognome = 'Inserisci il cognome';
            isValid = false;
        }

        // Validazione Data di Nascita
        if (!dataDiNascita) {
            errors.dataDiNascita = 'Inserisci la data di nascita';
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

        // Validazione Password (almeno 8 caratteri, una maiuscola, una numero e un carattere speciale)
        const passwordRegex = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;
        if (!password) {
            errors.password = 'Inserisci la password';
            isValid = false;
        } else if (!passwordRegex.test(password)) {
            errors.password = 'Formato password non valido: la password deve essere lunga 8 caratteri, contenere almeno una lettera maiuscola, un numero e un carattere speciale';
            isValid = false;
        }

        // Validazione Checkbox Termini e Condizioni
        if (!terms){
            errors.terms = "Devi accettare Termini e Condizioni";
            isValid = false;
        }

        setFieldErrors(errors);
        return isValid;
    };

    const performRegistration = async () => {
        try {
            /*
            const response = await fetch('http://172.19.183.142:8080/api/terapeuta/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome, cognome, dataDiNascita, email, password }),
                mode: 'no-cors',
            });

             */
            axios.defaults.baseURL = 'http://192.168.47.191:8080';
            axios.defaults.headers.post['Content-Type'] ='application/json;charset=utf-8';
            axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

            axios.post('/api/terapeuta/register', JSON.stringify({ nome, cognome, dataDiNascita, email, password }))
                .then(function (response) {
                    console.log(response);
                })
                .catch(function (error) {
                    console.log(error);
                });

        } catch (err) {
            setError(err.message || 'An unexpected error occurred.');
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
                <h2 className="title">Registration</h2>
                <br/>
                <Form.FloatingLabel controlId="registration.name" label="Nome">
                    <Form.Control type="text" value={nome} placeholder="" onChange={e => setNome(e.target.value)} />
                    {fieldErrors.nome && <p className="text-danger">{fieldErrors.nome}</p>}
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.surname" label="Cognome">
                    <Form.Control type="text" value={cognome} placeholder="" onChange={e => setCognome(e.target.value)} />
                    {fieldErrors.cognome && <p className="text-danger">{fieldErrors.cognome}</p>}
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.dateOfBirth" label="Data Di Nascita">
                    <Form.Control type="Date" value={dataDiNascita} placeholder="" onChange={e => setdataDiNascita(e.target.value)}/>
                    {fieldErrors.dataDiNascita && <p className="text-danger">{fieldErrors.dataDiNascita}</p>}
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.email" label="Email">
                    <Form.Control type="text" value={email} placeholder="" onChange={e => setEmail(e.target.value)}/>
                    {fieldErrors.email && <p className="text-danger">{fieldErrors.email}</p>}
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.password" label="Password">
                    <Form.Control type="password" value={password} placeholder="" onChange={e => setPassword(e.target.value)} />
                    {fieldErrors.password && <p className="text-danger">{fieldErrors.password}</p>}
                </Form.FloatingLabel>
                <br />

                <Form.Group controlId="login.persistence">
                    <Form.Check type="checkbox" label="Ho letto Termini e Condizioni" onChange={e => setTerms(e.target.checked)}/>
                    {fieldErrors.terms && <p className="text-danger">{fieldErrors.terms}</p>}
                </Form.Group>
                <br/>
                <Button variant="primary" type="submit">
                    Register
                </Button>
                <br />
                <Link to="/login" >Back To Login</Link>
            </Form>
        </Stack>
    );
};

export default Registration;

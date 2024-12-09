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
    const [error, setError] = useState('');

    const validateInputs = () => {
        if (!nome || !cognome || !dataDiNascita || !email || !password) {
            setError('All fields are required.');
            return false;
        }
        setError('');
        return true;
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
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.surname" label="Cognome">
                    <Form.Control type="text" value={cognome} placeholder="" onChange={e => setCognome(e.target.value)} />
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.dateOfBirth" label="Data Di Nascita">
                    <Form.Control type="Date" value={dataDiNascita} placeholder="" onChange={e => setdataDiNascita(e.target.value)}/>
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.email" label="Email">
                    <Form.Control type="email" value={email} placeholder="" onChange={e => setEmail(e.target.value)}/>
                </Form.FloatingLabel>
                <br />
                <Form.FloatingLabel controlId="registration.password" label="Password">
                    <Form.Control type="password" value={password} placeholder="" onChange={e => setPassword(e.target.value)} />
                </Form.FloatingLabel>
                <br />

                <Form.Group controlId="login.persistence">
                    <Form.Check type="checkbox" label="Ho letto Termini e Condizioni"/>
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

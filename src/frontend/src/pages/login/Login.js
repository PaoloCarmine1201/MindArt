import React, { useState } from 'react';
import {Link} from 'react-router-dom';
import {Form, Button, Stack} from 'react-bootstrap';
import "../../style/Login.css"
import axios from "axios";


const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const validateInputs = () => {
        if (!email || !password) {
            setError('Both fields are required.');
            return false;
        }
        setError('');
        return true;
    };

    const performLogin = async () => {
        try {
            console.log("Sending request");

            const response = await axios.post(
                "http://localhost:8080/auth/terapeuta/login",
                { email, password },
                {headers: {'Content-Type': 'application/json'}}
            );
            console.log("getting response");

            const data = response.data
            if(data === ""){
                throw Error("invalid username or password");
            }
            console.log('Login successful:', data);
            localStorage.setItem("jwtToken", data);

            // Redirect or handle login success
            window.location.href = '/';
        } catch (err) {
            console.log("Errore nella richiesta di login", err)
            setError(err.message || 'An unexpected error occurred.');
        }
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        if (validateInputs()) {
            await performLogin();
        }
    };
    // <img src={require('../../assets/logo_vertical_1024x1024_white.png')}  width="175" height="175" alt=""/>
    return (

        <Stack gap={4} className="login-container d-flex justify-content-center align-items-center vh-100" >
            <h1>{error}</h1>
            <Form onSubmit={handleLogin} className="card p-4 shadow-sm login-form">
                <h2 className="title">Login</h2>
                <br/>
                <Form.FloatingLabel controlId="login.email" label="Email address">
                    <Form.Control required type="email" placeholder="" value={email} onChange={e => setEmail(e.target.value)}/>
                </Form.FloatingLabel>
                <br/>
                <Form.FloatingLabel controlId="login.password" label="Password">
                    <Form.Control required type="password" placeholder="" value={password} onChange={e => setPassword(e.target.value)}/>
                </Form.FloatingLabel>
                <br/>
                <Form.Group controlId="login.persistence">
                    <Form.Check type="checkbox" label="Keep Me Signed In"/>
                </Form.Group>
                <br/>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
                <br />
                <Link to="/register" >Register</Link>
            </Form>

        </Stack>

    );
};

export default Login;

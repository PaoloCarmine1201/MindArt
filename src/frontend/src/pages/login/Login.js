import React, { useState } from 'react';
import {useNavigate} from 'react-router-dom';
import {Form, Button, Stack} from 'react-bootstrap';
import "../../style/Login.css"
import axios from "axios";
import {useAuth} from "../../auth/AuthProvider";
import {toast} from "react-toastify";


const Login = () => {
    const { login } = useAuth(); // Access the login function from AuthContext
    const navigate = useNavigate(); // For navigation
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
            toast.success("Login effettuato con successo.");
            login(data); // Update authentication state
            navigate("/"); // Redirect to a protected route
        } catch (err) {
            console.log("Errore nella richiesta di login", err)
            toast.error("Credenziali inserite errate, riprovare.");
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
    <>
        <Stack gap={4} className="login-container d-flex justify-content-center align-items-center vh-100" >
            <h1>{error}</h1>
            <Form onSubmit={handleLogin} className="card p-4 shadow-sm login-form">
                <h2 className="title">Login</h2>
                <br/>
                <Form.FloatingLabel controlId="login.email" label="Email">
                    <Form.Control required type="email" placeholder="" value={email}
                                  onChange={e => setEmail(e.target.value)}/>
                </Form.FloatingLabel>
                <br/>
                <Form.FloatingLabel controlId="login.password" label="Password">
                    <Form.Control required type="password" placeholder="" value={password}
                                  onChange={e => setPassword(e.target.value)}/>
                </Form.FloatingLabel>
                <br/>
                <div className="d-flex justify-content-end gap-2">
                    <Button
                        variant="primary"
                        className=" btn-disegno"
                        onClick={() => {
                            navigate("/register");
                        }}
                    >
                        Registrati
                    </Button>
                    <Button
                        variant="primary"
                        className="btn-conferma"
                        type="submit"
                    >
                        Invia
                    </Button>
                </div>
            </Form>
        </Stack>
    </>
    );

};

export default Login;

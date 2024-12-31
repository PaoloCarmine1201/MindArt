import React, { useEffect, useState } from 'react';
import '../../style/NavBarStyle.css';
import '../../style/Button.css'
import {Link, useNavigate} from 'react-router-dom';
import AvviaSessioneButton from "../AvvioSessione/AvviaSessioneButton";
import axiosInstance from "../../config/axiosInstance";
import logo from '../../assets/logo_horizontal_2048x1024.png';
import {Button} from "react-bootstrap";
import TerminaSessione from "../TerminaSessione/TerminaSessione";

function NavBar({ name }) {
    const [sessione, setSessione] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            await isActive();
        };
        fetchData();
    }, []);

    const isActive = async () => {
        try {
            const response = await axiosInstance.get('/api/terapeuta/sessione/');
            if (response && response.status === 200) {
                setSessione(true);
            } else {
                setSessione(false);
            }
        } catch (error) {
            console.error('Errore nel recupero della sessione:', error);
            setSessione(false);
        }
    };

    const handleLogout = async () => {
        try {
            const response = await axiosInstance.post('/auth/logout');
            if (response.status === 200) {
                // Rimuovi il token JWT dallo storage locale
                localStorage.removeItem('jwtToken');
                // Aggiorna lo stato della sessione
                setSessione(false);
                // Reindirizza l'utente alla pagina di login
                navigate('/login');
            } else {
                console.error('Errore durante il logout:', response);
            }
        } catch (error) {
            console.error('Errore durante il logout:', error);
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link to="/home">
                    <img src={logo} alt="Logo" className="logo" />
                </Link>
                <div className="vertical-bar"></div>
                <span className="page-title">{name}</span>
            </div>
            <div className="navbar-right">
                {sessione && localStorage.getItem("jwtToken")? (
                    <>
                        <TerminaSessione/>
                        <Link to="/terapeuta/draw" className="link">
                            <Button className="btn-all m-1">
                                Osserva
                            </Button>
                        </Link>
                    </>
                ) : (
                    <>
                        <AvviaSessioneButton />
                        <Button onClick={handleLogout} className="btn-cancella m-1">
                            Logout
                        </Button>
                    </>
                )}
                <div className="profile-icon">
                    <Link to="/profilo">
                        <img
                            src={require('../../assets/profile_icon.png')}
                            alt="Profile"
                            className="profile-icon"
                        />
                    </Link>
                </div>
            </div>
        </nav>
    );
}

export default NavBar;

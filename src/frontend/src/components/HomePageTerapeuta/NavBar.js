import React, { useEffect, useState } from 'react';
import '../../style/NavBarStyle.css';
import { Link } from 'react-router-dom';
import AvviaSessioneButton from "../AvvioSessione/AvviaSessioneButton";
import TerminaSessione from "../TerminaSessione/TerminaSessione";
import axiosInstance from "../../config/axiosInstance";
import logo from '../../assets/logo_horizontal_2048x1024.png';

function NavBar({ name }) {
    // SE IL TERAPEUTA HA UNA SESSIONE ATTIVA VIENE MOSTRATO OSSERVA E TERMINA SESSIONE
    // ALTRIMENTI VIENE MOSTRATO AVVIA SESSIONE
    const [sessione, setSessione] = useState(false);

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
            console.error("Errore nel recupero della sessione:", error);
            setSessione(false);
        }
    }

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
                {sessione ? (
                    <>
                        <TerminaSessione />
                        <Link to="/terapeuta/draw" className="link">
                            Osserva
                        </Link>
                    </>
                ) : (
                    <AvviaSessioneButton />
                )}

                <div className="profile-icon">
                    <img src={require('../../assets/profile_icon.png')} alt="Profile" className="profile-icon"/>
                </div>
            </div>
        </nav>
    );
};

export default NavBar;

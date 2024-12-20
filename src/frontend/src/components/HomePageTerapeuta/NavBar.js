import React from 'react';
import '../../style/NavBarStyle.css';
import { Link } from 'react-router-dom';

function NavBar({name}) {
    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link to={"/home"}><img src={require('../../assets/logo_horizontal_2048x1024.png')} alt="Logo" className="logo"/></Link>
                <div className="vertical-bar"></div>
                <span className="page-title">{name}</span>
            </div>
            <div className="navbar-right">
                <button className="start-session-button">Avvia sessione</button>
                <div className="profile-icon">
                    <img src={require('../../assets/profile_icon.png')} alt="Profile" className="profile-icon"/>
                </div>
            </div>
        </nav>
    );
};

export default NavBar;

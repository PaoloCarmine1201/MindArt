import TerapeutaEventiComponent from "../components/GestioneCalendario/TerapeutaEventiComponent";
import "../style/Calendar.css";
import NavBar from "../components/HomePageTerapeuta/NavBar";
import React from "react";


function GestioneEventiTerapeuta() {
    return (
        <>
            <NavBar name="I tuoi eventi"/>
            <TerapeutaEventiComponent/>
        </>
    );
}

export default GestioneEventiTerapeuta;
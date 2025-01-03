import NavBar from "../components/HomePageTerapeuta/NavBar";
import React from "react";
import DisegnoTerapeuta from "../components/Disegno/DisegnoTerapeuta";

function DisegnoInCorso() {
    return (
        <>
            <NavBar name="Disegno in Corso della sessione"/>
            <DisegnoTerapeuta sessionId={1}/>
        </>
    )
}

export default DisegnoInCorso;

import NavBar from "../components/HomePageTerapeuta/NavBar";
import React from "react";
import BoardDisegno from "../components/BoardDisegno";

function DisegnaBambino() {
    return (
        <>
            <NavBar name="Dashboard"/>
            <BoardDisegno sessionId={1} bambinoId={1}/>
        </>
    )
}

export default DisegnaBambino;
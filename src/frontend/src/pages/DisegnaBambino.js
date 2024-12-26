import NavBar from "../components/HomePageTerapeuta/NavBar";
import React from "react";
import DrawingBoard from "../components/DrawingBoard";

function DisegnaBambino() {
    return (
        <>
            <NavBar name="Dashboard"/>
            <DrawingBoard
                sessionId={1}
                childId={1}
            />
        </>
    )
}

export default DisegnaBambino;
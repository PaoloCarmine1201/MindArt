import React from "react";
import BambiniListComponent from "./BambiniListComponent";
import "../../style/VisualizzaBambiniStyle.css";

/**
 * @autor gabrieleristallo
 * componente per visualizzare i bambini in una lista formattata con un box
 */
function VisualizzaBambiniComponent({ bambini }) {
    return (
        <>
            <div className="visualizza-bambini-box">
                <div className={"list-box"}>
                    <BambiniListComponent bambini={bambini}/>
                </div>
            </div>
        </>

    );
}

export default VisualizzaBambiniComponent;

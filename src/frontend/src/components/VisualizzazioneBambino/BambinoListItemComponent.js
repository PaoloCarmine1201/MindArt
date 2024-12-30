import React from "react";
import "../../style/BambinoListItemStyle.css";

/**
 * @autor gabrieleristallo
 * @param props
 * componente utilizzato per la visualizzazione in una lista di un bambino
 */

function BambinoListItemComponent(props) {
    const { nome, cognome } = props.bambino;

    return (
        <div className="bambino-item">
            <p>{nome} {cognome}</p>→
        </div>
    );
}

export default BambinoListItemComponent;

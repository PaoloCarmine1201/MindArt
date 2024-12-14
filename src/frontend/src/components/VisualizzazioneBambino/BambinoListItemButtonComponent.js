import React from "react";
import "../../style/BambinoListItemButtonStyle.css";
import { Link } from 'react-router-dom';

function BambinoListItemButtonComponent(props) {
    const { id, nome, cognome } = props.bambino;

    return (
        <div className="bambino-item">
            {/* Contenuto principale */}
            <p>
                {nome} {cognome}
            </p>
            {/* Pulsanti sulla destra */}
            <div>
                <Link to={`/disegni/${id}`} className="bambino-item-link">
                    Disegni
                </Link>
                <Link to={`/elimina/${id}`} className="bambino-item-link eliminazione">
                    Elimina
                </Link>
            </div>
        </div>
    );
}

export default BambinoListItemButtonComponent;

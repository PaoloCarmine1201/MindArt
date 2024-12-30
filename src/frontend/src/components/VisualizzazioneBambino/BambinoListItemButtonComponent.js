import React from "react";
import "../../style/BambinoListItemButtonStyle.css";
import { Link } from 'react-router-dom';
import {Button} from "react-bootstrap";
import axiosInstance from "../../config/axiosInstance";
import "../../style/Button.css";

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
                {/* Voglio poter eliminare un bambino con un Button */}
                <Button className={"btn-cancella"} onClick={() => {
                    axiosInstance.delete(`http://localhost:8080/api/terapeuta/bambino/${id}`)
                        .then(response => {
                            console.log(response);
                        })
                        .catch(error => {
                            console.error(error);
                        });
                }
                }>
                    Elimina
                </Button>
            </div>
        </div>
    );
}

export default BambinoListItemButtonComponent;

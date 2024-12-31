import React, { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import '../../style/DettaglioBambinoStyle.css';
import axiosInstance from "../../config/axiosInstance";
import {Button} from "react-bootstrap";
import "../../style/Button.css";

function DettaglioBambinoComponent() {
    const { id } = useParams();
    const navigate = useNavigate();  // <--- Hook per navigare
    const [bambino, setBambino] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                console.log(id);
                const result = await fetch(
                    "http://localhost:8080/api/terapeuta/bambino/get/" + id
                );

                if (!result.ok) {
                    throw new Error(
                        "Errore nella risposta del server: " + result.status
                    );
                }

                const data = await result.json();
                console.log(data);
                setBambino(data);
            } catch (error) {
                console.error("Errore nel recupero dei dati:", error);
            }
        };

        fetchData();
    }, [id]);

    useEffect(() => {
        axiosInstance
            .get("api/terapeuta/bambino/get/" + id)
            .then((response) => {
                setBambino(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    }, [id]);

    if (!bambino) {
        return <p>Caricamento in corso...</p>;
    }

    const handleElimina = () => {
        axiosInstance
            .delete(`http://localhost:8080/api/terapeuta/bambino/${bambino.id}`)
            .then((response) => {
                console.log(response);
                // Dopo l’eliminazione, torniamo alla pagina precedente
                navigate(-1);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    return (
        <div className="dettaglio-container">
            <h2 className="dettaglio-header">
                {bambino.nome} {bambino.cognome}
            </h2>

            <div className="dettaglio-section">
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Codice:</label>
                    <span className="dettaglio-value">{bambino.codice}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Sesso:</label>
                    <span className="dettaglio-value">
                        {bambino.sesso === null
                            ? "Sesso non disponibile"
                            : bambino.sesso === "MASCHIO"
                                ? "Maschio"
                                : "Femmina"}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Data di Nascita:</label>
                    <span className="dettaglio-value">
                        {new Date(bambino.dataDiNascita).toLocaleDateString("it-IT")}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Codice Fiscale:</label>
                    <span className="dettaglio-value">{bambino.codiceFiscale}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Email Genitore:</label>
                    <span className="dettaglio-value">{bambino.emailGenitore}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Telefono Genitore:</label>
                    <span className="dettaglio-value">{bambino.telefonoGenitore}</span>
                </div>
            </div>

            <div className="dettaglio-button-container">
                {/* Pulsante Modifica */}
                <Link to={`/modifica/${id}`}>
                    <Button className="btn-all">
                        Modifica
                    </Button>
                </Link>

                {/* Pulsante Elimina */}
                <Button
                    className="btn-cancella"
                    style={{ marginLeft: "10px" }}
                    onClick={handleElimina}
                >
                    Elimina
                </Button>
            </div>

            <Link to={'/gestioneBambini'} className="dettaglio-button-link">
                <p className="dettaglio-back-button">↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default DettaglioBambinoComponent;

import { useEffect, useState } from "react";
import axiosInstance from "../../config/axiosInstance";
import '../../style/ProfiloTerapeutaStyle.css';
import { Link } from "react-router-dom";

function ProfiloTerapuetaComponent() {
    const [idTerapeuta, setIdTerapeuta] = useState(null);
    const [terapeuta, setTerapeuta] = useState(null);

    // Recupero dell'ID del terapeuta da localStorage
    useEffect(() => {
        const storedId = localStorage.getItem('idTerapeuta');
        if (storedId) {
            setIdTerapeuta(storedId);
        }
    }, []);

    // Chiamata API per ottenere i dati del terapeuta
    useEffect(() => {
        if (idTerapeuta) {
            axiosInstance.get(`api/terapeuta/get/${idTerapeuta}`)
                .then(response => {
                    setTerapeuta(response.data);
                })
                .catch(error => {
                    console.error("Errore durante il recupero dei dati del terapeuta:", error);
                });
        }
    }, [idTerapeuta]);

    // Contenuto mostrato durante il caricamento
    if (!terapeuta) {
        return <p>Caricamento in corso...</p>;
    }

    return (
        <div className="dettaglio-container">
            <h2 className="dettaglio-header">{terapeuta.nome} {terapeuta.cognome}</h2>
            <div className="dettaglio-section">
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Data di Nascita:</label>
                    <span className="dettaglio-value">
                        {new Date(terapeuta.dataDiNascita).toLocaleDateString('it-IT')}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Email:</label>
                    <span className="dettaglio-value">{terapeuta.email}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Sessioni effettuate:</label>
                    <span className="dettaglio-value">{terapeuta.numeroSessioni}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Bambini seguiti:</label>
                    <span className="dettaglio-value">{terapeuta.numeroBambini}</span>
                </div>
            </div>

            <div className="dettaglio-button-container">
                {/* Pulsante Modifica */}
                <Link to={`/modifica/terapeuta/${idTerapeuta}`} className="dettaglio-button-link-modifica">
                    Modifica
                </Link>
            </div>

            {/* Pulsante Indietro */}
            <Link to="/home" className="dettaglio-button-link">
                <p className="dettaglio-back-button">↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default ProfiloTerapuetaComponent;

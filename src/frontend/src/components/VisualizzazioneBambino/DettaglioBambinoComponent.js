import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import '../../style/DettaglioBambinoStyle.css';
import axiosInstance from "../../config/axiosInstance";
import EditBambino from "../GestioneInformazioniBambino/EditBambino";

/**
 * @autor gabrieleristallo
 * componente utilizzata per visualizzare i dettagli di un bambino
 * grazie ad useParams si ottiene l'id del bambino passato tramite url
 */
function DettaglioBambinoComponent() {
    const { id } = useParams();
    const [bambino, setBambino] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                console.log(id);
                const result = await fetch('http://localhost:8080/api/terapeuta/bambino/get/' + id);

                if (!result.ok) {
                    throw new Error('Errore nella risposta del server: ' + result.status);
                }

                const data = await result.json();
                console.log(data);
                setBambino(data);
            } catch (error) {
                console.error('Errore nel recupero dei dati:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        axiosInstance.get("api/terapeuta/bambino/get/" + id)
            .then(response => {
                setBambino(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    }, []);


    if (!bambino) {
        return <p>Caricamento in corso...</p>;
    }

    return (
        <div className="dettaglio-container">
            <h2 className="dettaglio-header">{bambino.nome} {bambino.cognome}</h2>

            <div className="dettaglio-section">
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Codice:</label>
                    <span className="dettaglio-value">{bambino.codice}</span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Sesso:</label>
                    <span className="dettaglio-value">
                        {bambino.sesso === null ? 'Sesso non disponibile' : bambino.sesso === 'MASCHIO' ?
                            'Maschio' : 'Femmina'}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Data di Nascita:</label>
                    <span className="dettaglio-value">
                        {new Date(bambino.dataDiNascita).toLocaleDateString('it-IT')}
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
                <EditBambino bambino={bambino}/>

                {/* Pulsante Elimina */}
                <Link to={`/elimina/${id}`} className="dettaglio-button-link-elimina">
                    Elimina
                </Link>
            </div>

            <Link to={'/gestioneBambini'} className="dettaglio-button-link">
                <p className="dettaglio-back-button">↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default DettaglioBambinoComponent;

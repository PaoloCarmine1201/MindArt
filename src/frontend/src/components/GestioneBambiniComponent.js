import { useState, useEffect } from 'react';
import BambinoListItemComponent from './BambinoListItemComponent.js';
import {Link} from "react-router-dom";
import '../style/GestioneBambini.css'

/**
 *
 * @autor gabrieleristallo
 * componente utilizzata per la visualizzazione e la gestione dei bambini
 * è possibile cliccare sui bambini per visualizzarne i dettagli
 * è possibile cliccare su aggiungi bambino per aggiungerne uno
 */
function GestioneBambiniComponent() {
    const [bambini, setBambini] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('http://localhost:8080/api/bambino/getall');
            console.log(result);
            const data = await result.json();
            console.log(data);
            setBambini(data);
        };
        fetchData();
    }, []);

    return (
        <div style={{backgroundColor: "#B8E1FF", padding: "7px", margin: "5px", borderRadius: "5px"}}>
            <h2 style={{color: "#f6f5e3", fontWeight: "bold"}}>Gestione Bambini</h2>
            <div className="container">
                {
                    bambini && bambini.length > 0 ? (
                        bambini.map((bambino) => (
                            <Link to={`/dettaglioBambino/${bambino.id}`} style={{textDecoration:"none"}}><BambinoListItemComponent key={bambino.id} bambino={bambino} /></Link>
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )
                }
            </div>
            <Link to={'/aggiungiBambino'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "right",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#f6f5e3",
                fontWeight: "bold"
            }}>Aggiungi Bambino →</p>
            </Link>
            <Link to={'/'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "left",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#f6f5e3",
                fontWeight: "bold"
            }}>↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default GestioneBambiniComponent;

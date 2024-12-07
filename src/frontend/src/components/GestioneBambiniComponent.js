import { useState, useEffect } from 'react';
import {Link} from "react-router-dom";
import '../style/GestioneBambini.css'
import BambiniListComponent from "./BambiniListComponent";

/**
 *
 * @autor gabrieleristallo
 * componente utilizzata per la visualizzazione e la gestione dei bambini
 * è possibile cliccare sui bambini per visualizzarne i dettagli
 * è possibile cliccare su aggiungi bambino per aggiungerne uno
 */
function GestioneBambiniComponent() {
    const [bambini, setBambini] = useState([]);

    const idTerapeuta = localStorage.getItem("idTerapeuta");

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('http://localhost:8080/api/bambino/getallbyterapeuta?terapeuta=' + idTerapeuta);
            console.log(result);
            const data = await result.json();
            console.log(data);
            setBambini(data);
        };
        fetchData();
    }, []);

    return (
        <>
            <BambiniListComponent bambini={bambini}/>
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
        </>
    );
}

export default GestioneBambiniComponent;

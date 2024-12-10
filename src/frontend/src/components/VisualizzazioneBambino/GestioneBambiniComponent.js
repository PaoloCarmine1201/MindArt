import { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import '../../style/GestioneBambiniStyle.css';
import BambiniListComponent from "./BambiniListComponent";

function GestioneBambiniComponent() {
    const [bambini, setBambini] = useState([]);

    const idTerapeuta = localStorage.getItem("idTerapeuta");

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('http://localhost:8080/api/bambino/getallbyterapeuta?terapeuta=' + idTerapeuta);
            const data = await result.json();
            setBambini(data);
        };
        fetchData();
    }, []);

    return (
        <div className="gestione-bambini-container">
            <div className="bambini-list-box">
                <BambiniListComponent bambini={bambini} button={true} from={"/gestioneBambini"}/>
                <div className="button-space">
                    <Link to={'/'} className="indietro-link">
                        ↩︎ Indietro
                    </Link>

                    <Link to={'/aggiungiBambino'} className="aggiungi-bambino-link">
                        Aggiungi Bambino →
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default GestioneBambiniComponent;

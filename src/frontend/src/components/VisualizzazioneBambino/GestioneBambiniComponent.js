import { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import '../../style/GestioneBambiniStyle.css';
import BambiniListComponent from "./BambiniListComponent";
import RegisterBambino from "../GestioneInformazioniBambino/RegisterBambino";
import axiosInstance from "../../config/axiosInstance";

function GestioneBambiniComponent() {
    const [bambini, setBambini] = useState([]);

    useEffect(() => {
        axiosInstance.get("http://localhost:8080/api/terapeuta/bambino/getallbyterapeuta")
            .then(response => {
                setBambini(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    }, []);

    return (
        <div className="gestione-bambini-container">
            <div className="bambini-list-box">
                <BambiniListComponent bambini={bambini} button={true} from={"/gestioneBambini"}/>
                <div className="button-space">
                    <Link to={'/'} className="indietro-link">
                        ↩︎ Indietro
                    </Link>

                    <RegisterBambino/>
                </div>
            </div>
        </div>
    );
}

export default GestioneBambiniComponent;

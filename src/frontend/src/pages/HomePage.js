import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import NavBar from "../components/HomePageTerapeuta/NavBar";
import "../style/HomaPageStyle.css";
import BambiniListComponent from "../components/VisualizzazioneBambino/BambiniListComponent";
import VisualizzaEventiComponent from "../components/GestioneCalendario/VisualizzaEventiComponent";
import axiosInstance from "../config/axiosInstance";
import TerminaSessione from "../components/TerminaSessione/TerminaSessione";
import GestioneMaterialeWidget from "../components/GestioneMateriale/GestioneMaterialeWidget";
import ToastNotification from "../components/Notification/Notification";

function HomePage() {
    const [idTerapeuta, setIdTerapeuta] = useState(1); // id del terapeuta loggato DA MODIFICARE
    const [bambini, setBambini] = useState([]);
    const [bambiniError, setBambiniError] = useState(null);

    // Store idTerapeuta in localStorage when it changes
    useEffect(() => {
        localStorage.setItem("idTerapeuta", idTerapeuta);
    }, [idTerapeuta]);

    // Fetch bambini data when the component mounts
    useEffect(() => {
        const fetchBambini = async () => {
            try {
                const response = await axiosInstance.get('/api/terapeuta/bambino/getallbyterapeuta');
                setBambini(response.data);
            } catch (error) {
                setBambiniError('Errore nel caricamento dei bambini.');
                console.error("Error fetching bambini:", error);
            }
        };

        fetchBambini();
    }, []);


    return (
        <>
            <ToastNotification />
            <NavBar name="Dashboard"/>
            <div className="app-container">

                <div className="content-container">
                    <div className="box eventi">
                        <Link to={"/calendarioEventi"} className={"link"}><h2>Prossimi eventi</h2></Link>
                        <div className="item-container">
                            <VisualizzaEventiComponent/>
                        </div>
                    </div>

                    <div className="right-box">
                        <div className="box pazienti">
                            <Link to={"/gestioneBambini"} className={"link"}><h2>I tuoi pazienti</h2></Link>
                            <div className="item-container">
                                <BambiniListComponent bambini={bambini}  button={false}/>
                            </div>
                        </div>

                        <div className="box materiali">
                            <Link to={"/gestioneMateriale"} className={"link"}><h2>I tuoi materiali</h2></Link>
                            <div className={"item-container"}>
                                <GestioneMaterialeWidget/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <ToastContainer position="bottom-right"/>
        </>
    );
}

export default HomePage;

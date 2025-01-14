import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import NavBar from "../components/HomePageTerapeuta/NavBar";
import "../style/HomaPageStyle.css";
import BambiniListComponent from "../components/VisualizzazioneBambino/BambiniListComponent";
import VisualizzaEventiComponent from "../components/GestioneCalendario/VisualizzaEventiComponent";
import axiosInstance from "../config/axiosInstance";
import GestioneMaterialeWidget from "../components/GestioneMateriale/GestioneMaterialeWidget";

function HomePage() {
    const [bambini, setBambini] = useState([]);



    // Fetch bambini data when the component mounts
    useEffect(() => {
        const fetchBambini = async () => {
            try {
                const response = await axiosInstance.get('/api/terapeuta/bambino/getallbyterapeuta');
                setBambini(response.data);
            } catch (error) {
                console.error("Error fetching bambini:", error);
            }
        };

        fetchBambini();
    }, []);


    return (
        <>
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
        </>
    );
}

export default HomePage;

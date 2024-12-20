import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import NavBar from "../components/HomePageTerapeuta/NavBar";
import "../style/HomaPageStyle.css";
import BambiniListComponent from "../components/VisualizzazioneBambino/BambiniListComponent";
import VisualizzaEventiComponent from "../components/GestioneCalendario/VisualizzaEventiComponent";

function HomePage() {
    const [idTerapeuta, setIdTerapeuta] = useState(1); // id del terapeuta loggato DA MODIFICARE
    localStorage.setItem("idTerapeuta", idTerapeuta);
    const [bambini, setBambini] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch(
                'http://localhost:8080/api/bambino/getallbyterapeuta?terapeuta=' + idTerapeuta
            );
            const data = await result.json();
            setBambini(data);
        };
        fetchData();
    }, [idTerapeuta]);

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
                            <Link to={"/materiale"} className={"link"}><h2>I tuoi materiali</h2></Link>
                            <div className={"item-container"}>
                                {/*Materiali component*/}
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

import './App.css';
import React, {useEffect, useState} from 'react';
import VisualizzaBambiniComponent from './components/VisualizzazioneBambino/VisualizzaBambiniComponent';
import { Link } from "react-router-dom";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import TerapeutaEventiComponent from "./components/GestioneCalendario/TerapeutaEventiComponent";

function App() {
    const [idTerapeuta, setIdTerapeuta] = useState(1); //id del terapeuta loggato DA MODIFICARE
    localStorage.setItem("idTerapeuta", idTerapeuta);
    const [bambini, setBambini] = useState([]);

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
          <div className="app-container">
              {/* Navbar */}
              <header className="navbar">
                  <div className="navbar-logo">MindArt</div>
                  <div className="navbar-title">Dashboard</div>
                  <button className="navbar-button">Avvia sessione</button>
                  <div className="navbar-profile"><Link to={"/login"} style={{textDecoration: "none"}} /></div>
              </header>

              {/* Main content */}
              <div className="content">
                  <div className="left-column">
                      <div className="box" id="box-pazienti">
                          <Link to={"/gestioneBambini"} style={{textDecoration: "none"}}>
                              <VisualizzaBambiniComponent bambini={bambini}/>
                          </Link>
                      </div>
                      <div className="box" id="box-materiali">
                          <h3>I tuoi materiali</h3>
                      </div>
                  </div>
                  <div className="right-column">
                      <div className="box" id="box-sessione">
                          <TerapeutaEventiComponent/>
                      </div>
                  </div>
              </div>
            </div>
          <ToastContainer position="bottom-right"/>
      </>
  );
}

export default App;

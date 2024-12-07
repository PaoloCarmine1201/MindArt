import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {Link} from "react-router-dom";
import BambiniListComponent from "./BambiniListComponent";

/**
 * @autor gabrieleristallo
 * Componente destinata ad essere usata nella home page del terapeuta
 * consente la visualizzazione generica di un numero limitato di bambini
 * un link offre la possibilità di accedere alla pagina per la propria gestione
 */

function VisualizzaBambiniComponent(props) {
    const bambini = props.bambini;

    return (
        <>
            <BambiniListComponent bambini={bambini}/>
                <Link to={'/gestioneBambini'}
                      style={{textDecoration: "none", fontWeight: "bold"}}
                      state={bambini}><p style={{
                    textAlign: "right",
                    textDecoration: "none",
                    marginTop: "10px",
                    cursor: "pointer",
                    paddingRight: "10px",
                    color: "#2c3e50"
                }}>Vedi tutto →</p></Link>
        </>
    );
}

export default VisualizzaBambiniComponent;
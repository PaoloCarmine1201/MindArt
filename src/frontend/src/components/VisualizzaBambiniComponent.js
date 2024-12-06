import {useEffect, useState} from "react";
import BambinoListItemComponent from "./BambinoListItemComponent";
import {Link} from "react-router-dom";

/**
 * @autor gabrieleristallo
 * Componente destinata ad essere usata nella home page del terapeuta
 * consente la visualizzazione generica di un numero limitato di bambini
 * un link offre la possibilità di accedere alla pagina per la propria gestione
 */

function VisualizzaBambiniComponent() {
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
            <h2 style={{color: "#f6f5e3", fontWeight: "bold"}}>I tuoi pazienti</h2>
            <div className="container">
                {
                    bambini && bambini.length > 0 ? (
                        bambini.slice(0, 3).map((bambino) => (
                            <BambinoListItemComponent key={bambino.id} bambino={bambino} />
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )
                }
            </div>
            <Link to={'/gestioneBambini'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "right",
                textDecoration: "none",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#2c3e50"
            }}>Vedi tutto →</p></Link>
        </div>
    );
}

export default VisualizzaBambiniComponent;
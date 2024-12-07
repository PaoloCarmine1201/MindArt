import { Link } from "react-router-dom";
import BambinoListItemComponent from "./BambinoListItemComponent";

function BambiniListComponent({ bambini }) {
    return (
        <>
            <div style={{ backgroundColor: "#B8E1FF", padding: "7px", margin: "5px", borderRadius: "5px" }}>
                <h2 style={{ color: "#f6f5e3", fontWeight: "bold" }}>Gestione Bambini</h2>
                <div className="container">
                    {bambini && bambini.length > 0 ? (
                        bambini.map((bambino) => (
                            <Link
                                key={bambino.id}
                                to={`/dettaglioBambino/${bambino.id}`}
                                style={{ textDecoration: "none" }}
                            >
                                <BambinoListItemComponent bambino={bambino} />
                            </Link>
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )}
                </div>
            </div>
        </>
    );
}

export default BambiniListComponent;

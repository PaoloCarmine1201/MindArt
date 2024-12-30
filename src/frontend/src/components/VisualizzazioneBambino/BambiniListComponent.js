import { Link } from "react-router-dom";
import BambinoListItemComponent from "./BambinoListItemComponent";
import "../../style/BambiniListStyle.css";
import { Button } from "react-bootstrap";
import React from "react";
import GestioneListaDisegno from "../GestioneListaDisegno/GestioneListaDisegno";

function BambiniListComponent(props) {
    console.log(props);
    const { bambini, button } = props;

    return (
        <>
            <div className="bambini-list-container">
                {bambini && bambini.length > 0 ? (
                    bambini.map((bambino) => (
                        <div key={bambino.id}>
                            <div style={{ display: "flex", alignItems: "center" }}>

                                {/* Link ai details del bambino */}
                                <Link
                                    to={`/dettaglioBambino/${bambino.id}`}
                                    className="bambino-link"
                                    style={{ flex: 1, borderRadius: "10px" }}
                                >
                                    <BambinoListItemComponent bambino={bambino} />
                                </Link>

                                {/* Se button è true, allora visualizzo i pulsanti (utilizzato per differenziare tra widget e fullpage */}
                                {button ? (
                                    <>
                                        <Link
                                            to={`/dettaglioDisegni/${bambino.id}`}
                                            style={{ borderRadius: "10px", marginLeft: "10px" }}
                                        >
                                            <Button className="btn-all">
                                                Visualizza disegni
                                            </Button>
                                        </Link>
                                    </>
                                ) : null}
                            </div>

                            <hr className="separatore" />
                        </div>
                    ))
                ) : (
                    <p>Nessun bambino trovato</p>
                )}
            </div>
        </>
    );
}

export default BambiniListComponent;

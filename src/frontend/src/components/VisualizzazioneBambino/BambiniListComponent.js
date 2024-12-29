import { Link } from "react-router-dom";
import BambinoListItemComponent from "./BambinoListItemComponent";
import "../../style/BambiniListStyle.css";
import { Button } from "react-bootstrap";
import axiosInstance from "../../config/axiosInstance";
import React from "react";

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
                                        <Button
                                            className="btn-conferma"
                                            style={{ marginLeft: "10px" }}
                                            onClick={() => {
                                                axiosInstance
                                                    .delete(
                                                        `http://localhost:8080/api/terapeuta/bambino/${bambino.id}`
                                                    )
                                                    .then((response) => {
                                                        console.log(response);
                                                    })
                                                    .catch((error) => {
                                                        console.error(error);
                                                    });
                                            }}
                                        >
                                            Disegni
                                        </Button>
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

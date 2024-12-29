// GestioneListaDisegno.js
import React, { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import "../../style/BambiniListStyle.css";
import "../../style/Button.css";
import MostraDisegnoBambino from "./MostraDisegnoBambino";
import { useParams } from "react-router-dom";
import axiosInstance from "../../config/axiosInstance";

function GestioneListaDisegno() {
    const { id } = useParams();
    const [disegni, setDisegni] = useState([]);
    const [disegnoId, setDisegnoId] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchDisegni = async () => {
            try {
                const response = await axiosInstance.get(
                    `/api/terapeuta/bambino/${id}/disegni/`
                );
                setDisegni(response.data);
            } catch (err) {
                setError(err);
            }
        };

        if (id) {
            fetchDisegni();
        }
    }, [id]);

    if (error) {
        return (
            <div className="disegni-list-container">
                <p style={{ color: "red" }}>
                    Errore durante il caricamento dei disegni: {error.message}
                </p>
            </div>
        );
    }

    return (
        <>
            <div className="disegni-list-container">
                {disegni && disegni.length > 0 ? (
                    disegni.map((disegno) => (
                        <div key={disegno.id}>
                            <div style={{ display: "flex", alignItems: "center" }}>
                                <div>
                                    <h4 style={{ margin: 0 }}>{disegno.temaAssegnato}</h4>
                                    <p>Data: {disegno.data}</p>
                                </div>

                                <Button
                                    className="btn-disegno"
                                    style={{ marginLeft: "10px" }}
                                    onClick={() => {
                                        setDisegnoId(disegno.id);
                                    }}
                                >
                                    Visualizza
                                </Button>
                            </div>
                            <hr className="separatore" />
                        </div>
                    ))
                ) : (
                    <p>Nessun disegno trovato</p>
                )}
            </div>
            <MostraDisegnoBambino disegnoId={disegnoId} />
        </>
    );
}

export default GestioneListaDisegno;
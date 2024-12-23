import {Button, Card} from "react-bootstrap";
import {FaFile} from "react-icons/fa";
import React, {useState} from "react";

import '../../style/Button.css';
import axiosInstance from "../../config/axiosInstance";

function GestioneMateriale() {

    const [filter, setFilter] = useState('ALL');
    const [materialList, setMaterialList] = useState([]);
    const [loadingMaterial, setLoadingMaterial] = useState(false);
    const [materialError, setMaterialError] = useState(null);


    axiosInstance.get('http://localhost:8080/api/terapeuta/materiale/getallbyterapeuta')
        .then(response => {
            setMaterialList(response.data);
            setLoadingMaterial(false);
        })
        .catch(error => {
            setMaterialError('Errore nel caricamento dei materiali.');
            setLoadingMaterial(false);
        });


    // Filtra i materiali in base al filtro selezionato
    const filteredMaterials = materialList.filter((mat) => {
        if (filter === 'ALL') return mat;
        return mat.tipoMateriale === filter;
    });

    return (
        <div>
            {/* Filtri in cima */}
            <div className="d-flex mb-3">
                <Button
                    variant={filter === "ALL" ? "btn-outline-primary btn-cancella-full" : "btn-outline-primary btn-cancella"}
                    onClick={() => setFilter("ALL")}
                    className="me-2"
                >
                    ALL
                </Button>
                <Button
                    variant={filter === "PDF" ? "btn-outline-primary btn-cancella-full" : "btn-outline-primary btn-cancella"}
                    onClick={() => setFilter("PDF")}
                    className="me-2"
                >
                    PDF
                </Button>
                <Button
                    variant={filter === "VIDEO" ? "btn-outline-primary btn-annulla-full" : "btn-outline-primary btn-annulla"}
                    onClick={() => setFilter("VIDEO")}
                    className="me-2"
                >
                    Video
                </Button>
                <Button
                    variant={filter === "IMMAGINE" ? "btn-outline-primary btn-conferma-full" : "btn-outline-primary btn-conferma"}
                    onClick={() => setFilter("IMMAGINE")}
                >
                    Immagine
                </Button>
            </div>

            {/* Schede Materiali */}
            <div className="d-flex flex-wrap">
                {filteredMaterials.map((mat) => (
                    <Card
                        key={mat.id}
                        className={`m-2 selectable-card`}
                        style={{
                            cursor: "pointer",
                            border: "1px solid #ddd",
                            width: "120px",
                            textAlign: "center",
                        }}
                    >
                        <Card.Body>
                            {/* Icona del file */}
                            <FaFile size={40} style={{color: "#6c757d"}}/>
                            {/* Nome del materiale */}
                            <Card.Text
                                className="mt-2 card-title"
                                style={{
                                    whiteSpace: "nowrap",
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                    fontSize: "14px",
                                }}
                                title={mat.nome} // Mostra il titolo completo al passaggio del mouse
                            >
                                {mat.nome}
                            </Card.Text>
                        </Card.Body>
                    </Card>
                ))}
            </div>
        </div>
    );
}

export default GestioneMateriale;
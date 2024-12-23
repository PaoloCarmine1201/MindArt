import { Button, Card, Spinner } from "react-bootstrap";
import React, {isValidElement, useEffect, useState} from "react";
import { useFormikContext } from "formik";
import { FaFile } from "react-icons/fa";

import '../../style/Button.css';

function SelezioneMateriale({ materialList, loading }) {
    const { values, setFieldValue } = useFormikContext();
    const tipoSessione = values.tipoSessione;
    const [filter, setFilter] = useState((tipoSessione === 'COLORE'? 'IMMAGINE' : 'PDF'));

    // Configura i filtri disabilitati in base al tipoSessione
    const disabledFilters = (() => {
        if (tipoSessione === "COLORE") return ["PDF", "VIDEO"];
        if (tipoSessione === "APPRENDIMENTO") return ["IMMAGINE"];
        return [];
    })();

    // Reset della selezione del file quando il componente viene montato
    useEffect(() => {
        setFieldValue("materiale", ""); // Resetta la selezione del file
    }, [setFieldValue]);

    const handleCardClick = (matId) => {
        setFieldValue("materiale", matId);
    };

    const handleFilterChange = (filterType) => {
        if (!disabledFilters.includes(filterType)) {
            setFilter(filterType);
        }
    };

    // Filtra i materiali in base al filtro selezionato
    const filteredMaterials = materialList.filter((mat) => {
        return mat.tipoMateriale === filter; // Mostra solo i materiali del tipo selezionato
    });

    if (loading) {
        return (
            <div className="d-flex align-items-center">
                <Spinner animation="border" size="sm" className="me-2" />
                Caricamento...
            </div>
        );
    }

    return (
        <div>
            {/* Filtri in cima */}
            <div className="d-flex mb-3">

                <Button
                    variant={filter === "PDF" ? "btn-outline-primary btn-cancella-full" : "btn-outline-primary btn-cancella"}
                    onClick={() => handleFilterChange("PDF")}
                    className="me-2"
                    disabled={disabledFilters.includes("PDF")} // Disabilita se il filtro è nella lista disabilitata
                >
                    PDF
                </Button>
                <Button
                    variant={filter === "VIDEO" ? "btn-outline-primary btn-annulla-full" : "btn-outline-primary btn-annulla"}
                    onClick={() => handleFilterChange("VIDEO")}
                    className="me-2"
                    disabled={disabledFilters.includes("VIDEO")} // Disabilita se il filtro è nella lista disabilitata
                >
                    Video
                </Button>
                <Button
                    variant={filter === "IMMAGINE" ? "btn-outline-primary btn-conferma-full" : "btn-outline-primary btn-conferma"}
                    onClick={() => handleFilterChange("IMMAGINE")}
                    disabled={disabledFilters.includes("IMMAGINE")} // Disabilita se il filtro è nella lista disabilitata
                >
                    Immagine
                </Button>
            </div>

            {/* Schede Materiali */}
            <div className="d-flex flex-wrap">
                {filteredMaterials.map((mat) => (
                    <Card
                        key={mat.id}
                        className={`m-2 selectable-card ${values.materiale === mat.id ? "selected" : ""}`}
                        onClick={() => handleCardClick(mat.id)}
                        style={{
                            cursor: "pointer",
                            border: values.materiale === mat.id ? "2px solid green" : "1px solid #ddd",
                            width: "120px",
                            textAlign: "center",
                        }}
                    >
                        <Card.Body>
                            {/* Icona del file */}
                            <FaFile size={40} style={{ color: "#6c757d" }} />
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

            {/* Nome del file selezionato */}
            <div className="mt-4 text-center" style={{ textAlign: "right", marginRight: "10px" }}>
                {values.materiale ? (
                    <p
                        style={{
                            fontSize: "12px", // Riduci leggermente la dimensione del testo
                            color: "#6c757d", // Colore grigio per un aspetto più elegante
                        }}
                    >
                        <strong>File selezionato:</strong>{" "}
                        <span
                            style={{
                                textDecoration: "underline", // Sottolinea solo il nome del file
                            }}
                        >
                            {materialList.find((mat) => mat.id === values.materiale)?.nome}
                        </span>
                    </p>
                ) : (
                    <p
                        style={{
                            fontSize: "12px", // Riduci leggermente la dimensione del testo
                            color: "#6c757d", // Colore grigio per un aspetto più elegante
                        }}
                    >
                        <strong>Seleziona un file per visualizzarne il nome.</strong>
                    </p>
                )}
            </div>
        </div>
    );
}

export default SelezioneMateriale;

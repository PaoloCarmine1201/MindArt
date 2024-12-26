import { Card } from "react-bootstrap";
import { FaFile, FaTrashAlt } from "react-icons/fa";
import React from "react";

function MaterialeCard({ id, nome, onDelete, large = false,  showDelete = true }) {
    return (
        <Card
            key={id}
            className={`m-1 selectable-card ${large ? "large-card" : ""}`}
            style={{
                cursor: "pointer",
                border: "1px solid #ddd",
                width: large ? "150px" : "100px", // Card più grande
                textAlign: "center",
                position: "relative",
                borderRadius: "8px",
                boxShadow: "0px 2px 5px rgba(0, 0, 0, 0.1)",
            }}
        >
            <Card.Body>
                {/* Icona del file */}
                <FaFile size={large ? 50 : 36} style={{ color: "#6c757d" }} />
                {/* Nome del materiale */}
                <Card.Text
                    className="mt-2 card-title"
                    style={{
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        fontSize: large ? "16px" : "12px",
                    }}
                    title={nome}
                >
                    {nome}
                </Card.Text>
                {showDelete && ( // Condizione per mostrare o nascondere l'icona del cestino
                    <FaTrashAlt
                        size={large ? 20 : 16}
                        style={{
                            position: "absolute",
                            bottom: "5px",
                            right: "5px",
                            cursor: "pointer",
                            color: "#dc3545",
                        }}
                        onClick={() => onDelete(id)} // Callback alla funzione di eliminazione
                        title="Elimina materiale"
                    />
                )}
            </Card.Body>
        </Card>
    );
}

export default MaterialeCard;

import {Card} from "react-bootstrap";
import {FaFile, FaTrashAlt} from "react-icons/fa";
import React from "react";

function MaterialeCard({id, nome, onDelete}) {
    return (
        <Card
            key={id}
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
                    title={nome}
                >
                    {nome}
                </Card.Text>
                <FaTrashAlt
                    size={18}
                    style={{
                        position: "absolute",
                        bottom: "5px",
                        right: "5px",
                        cursor: "pointer",
                        color: "#dc3545", // Colore rosso
                    }}
                    onClick={() => onDelete(id)} // Callback alla funzione di eliminazione
                    title="Elimina materiale"
                />
            </Card.Body>
        </Card>
    );
}

export default MaterialeCard;
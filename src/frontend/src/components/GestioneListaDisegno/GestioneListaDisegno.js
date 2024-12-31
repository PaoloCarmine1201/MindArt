import React, { useEffect, useState } from "react";
import { Button, Row, Col, Container } from "react-bootstrap";
import "../../style/BambiniListStyle.css";
import "../../style/Button.css";
import MostraDisegnoBambino from "./MostraDisegnoBambino";
import { useParams } from "react-router-dom";
import axiosInstance from "../../config/axiosInstance";
import NavBar from "../HomePageTerapeuta/NavBar";

function GestioneListaDisegno() {
    const { id } = useParams();
    const [disegni, setDisegni] = useState([]);
    const [disegnoId, setDisegnoId] = useState(null);
    const [showBoard, setShowBoard] = useState(false);

    useEffect(() => {
        const fetchDisegni = async () => {
            try {
                const response = await axiosInstance.get(
                    `/api/terapeuta/bambino/${id}/disegni/`
                );
                setDisegni(response.data);
            } catch (err) {
                console.log(err);
            }
        };

        if (id) {
            fetchDisegni();
        }
    }, [id]);

    const formatDate = (dateString) => {
        const options = { day: "2-digit", month: "long", year: "numeric", hour: "2-digit", minute: "2-digit" };
        return new Date(dateString).toLocaleDateString("it-IT", options);
    };

    return (
        <>
            <NavBar />

            <Container className="disegni-list-container text-center">
                {disegni && disegni.length > 0 ? (
                    <>
                        <Row className="py-3 border-bottom table-header">
                            <Col md={1} className="table-header-cell fw-bold">#</Col>
                            <Col md={4} className="table-header-cell fw-bold">Tema Assegnato</Col>
                            <Col md={3} className="table-header-cell fw-bold">Data</Col>
                            <Col md={2} className="table-header-cell fw-bold">Valutazione</Col>
                            <Col md={2} className="table-header-cell fw-bold"></Col>
                        </Row>
                        {disegni.map((disegno, index) => (
                            <Row
                                key={disegno.id}
                                className="align-items-center py-3 border-bottom table-row"
                            >
                                <Col md={1} className="fw-bold table-cell">
                                    {index + 1}
                                </Col>
                                <Col md={4} className="table-cell">
                                    <span>{disegno.tema}</span>
                                </Col>
                                <Col md={3} className="text-muted table-cell">
                                    {formatDate(disegno.data)}
                                </Col>
                                <Col md={2} className="text-muted table-cell">
                                    {disegno.voto ? disegno.voto : "Non valutato"}
                                </Col>
                                <Col md={2} className="table-cell">
                                    <Button
                                        variant="primary"
                                        size="md"
                                        className="btn-disegno"
                                        onClick={() => {
                                            setDisegnoId(disegno.id);
                                            setShowBoard(true);
                                        }}
                                    >
                                        Visualizza
                                    </Button>
                                </Col>
                            </Row>
                        ))}
                    </>
                ) : (
                    <p className="text-center text-muted">Nessun disegno trovato</p>
                )}
            </Container>

            { showBoard && (
                <MostraDisegnoBambino disegnoId={disegnoId} />
            )}
        </>
    );
}

export default GestioneListaDisegno;

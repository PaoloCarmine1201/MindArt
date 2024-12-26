import NavBar from "../HomePageTerapeuta/NavBar";
import React from "react";
import { Container, Row, Col, Button, Card } from "react-bootstrap";
import GestioneMaterialeBase from "./GestioneMaterialeBase";
import FileUploader from "./FileUploader";
import MaterialeCard from "./MaterialeCard";
import axiosInstance from "../../config/axiosInstance";
import {Link} from "react-router-dom";
import {FaBoxOpen} from "react-icons/fa";

class GestioneMaterialeFull extends GestioneMaterialeBase {
    handleDeleteMaterial = (id) => {
        axiosInstance
            .delete("http://localhost:8080/api/terapeuta/materiale/" + id)
            .then((response) => {
                if (response.status === 200) {
                    this.setState((prevState) => ({
                        materialList: prevState.materialList.filter((material) => material.id !== id),
                    }));
                }
            })
            .catch((error) => {
                console.log(error);
            });
    };

    handleFileAdded = (data) => {
        this.setState((prevState) => ({
            materialList: [...prevState.materialList, data],
        }));
    };

    render() {
        const filteredMaterials = this.getFilteredMaterials();

        return (
            <>
                {/* Aggiunta della NavBar */}
                <NavBar />

                {/* Contenuto principale */}
                <Container className="py-4">
                    <Card className="shadow-sm p-4" style={{ border: "2px solid #ddd", borderRadius: "10px" }}>
                        {/* Sezione Filtri */}
                        <Row className="mb-3">
                            <Col>
                                <div className="filters-container">
                                    {this.renderFilters()}
                                </div>
                            </Col>
                        </Row>

                        {/* Lista dei materiali o messaggio vuoto */}
                        {filteredMaterials.length === 0 ? (
                            <Row className="text-center">
                                <Col>
                                    <div className="mb-4" style={{ fontSize: "18px", color: "#6c757d" }}>
                                        <FaBoxOpen size={50} style={{ color: "#6c757d" }} />
                                        <p>Nessun materiale trovato</p>
                                    </div>
                                </Col>
                            </Row>
                        ) : (
                            <Row className="g-0"> {/* Riduzione dello spazio tra le card */}
                                {filteredMaterials.map((mat) => (
                                    <Col key={mat.id} md={0} sm={2} xs={6}> {/* Larghezza maggiore delle card */}
                                        <MaterialeCard
                                            id={mat.id}
                                            nome={mat.nome}
                                            onDelete={() => this.handleDeleteMaterial(mat.id)}
                                            large // Passiamo la prop per rendere le card più grandi
                                            showDelete={true} // Mostra l'icona del cestino
                                        />
                                    </Col>
                                ))}
                            </Row>
                        )}

                        {/* Footer con pulsanti */}
                        <Row className="mt-4">
                            <Col xs={6} className="text-start">
                                <Link to={'/home'} className="dettaglio-button-link">
                                    <p className="dettaglio-back-button">↩︎ Indietro</p>
                                </Link>
                            </Col>
                            <Col xs={6} className="text-end">
                                <FileUploader onUpload={this.handleFileAdded} />
                            </Col>
                        </Row>
                    </Card>
                </Container>
            </>
        );
    }
}

export default GestioneMaterialeFull;

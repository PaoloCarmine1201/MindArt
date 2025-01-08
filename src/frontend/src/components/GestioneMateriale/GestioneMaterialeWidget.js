import React from "react";
import GestioneMaterialeBase from "./GestioneMaterialeBase";
import MaterialeCard from "./MaterialeCard";
import {FaBoxOpen} from "react-icons/fa";
import {Col, Row} from "react-bootstrap";

class GestioneMaterialeWidget extends GestioneMaterialeBase {
    render() {
        const filteredMaterials = this.getFilteredMaterials();

        return (
            <>
                {/* Filtri */}
                {this.renderFilters()}

                {/* Lista Materiali */}
                <div className="d-flex flex-wrap" style={{ marginTop: "10px"}}>
                    {Array.isArray(filteredMaterials) && filteredMaterials.length > 0 ? (
                        filteredMaterials.map((mat) => (
                            <MaterialeCard
                                key={mat.id}
                                id={mat.id}
                                nome={mat.nome}
                                onDelete={null}
                                showDelete={false}
                            />
                        ))
                    ) : (
                        <Row className="text-center">
                            <Col>
                                <div className="mb-4" style={{ fontSize: "18px", color: "#6c757d" }}>
                                    <FaBoxOpen size={50} style={{ color: "#6c757d" }} />
                                    <p>Nessun materiale trovato</p>
                                </div>
                            </Col>
                        </Row>
                    )}
                </div>
            </>
        );
    }
}

export default GestioneMaterialeWidget;

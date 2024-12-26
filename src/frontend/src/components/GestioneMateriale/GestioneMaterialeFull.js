import React from "react";
import GestioneMaterialeBase from "./GestioneMaterialeBase";
import FileUploader from "./FileUploader";
import MaterialeCard from "./MaterialeCard";
import axiosInstance from "../../config/axiosInstance";

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
                {/* Filtri */}
                {this.renderFilters()}

                {/* FileUploader */}
                <FileUploader onUpload={this.handleFileAdded} />

                {/* Lista Materiali */}
                <div className="d-flex flex-wrap">
                    {filteredMaterials.map((mat) => (
                        <MaterialeCard
                            key={mat.id}
                            id={mat.id}
                            nome={mat.nome}
                            onDelete={() => this.handleDeleteMaterial(mat.id)}
                        />
                    ))}
                </div>
            </>
        );
    }
}

export default GestioneMaterialeFull;

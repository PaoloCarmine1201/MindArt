import React from "react";
import GestioneMaterialeBase from "./GestioneMaterialeBase";
import MaterialeCard from "./MaterialeCard";

class GestioneMaterialeWidget extends GestioneMaterialeBase {
    render() {
        const filteredMaterials = this.getFilteredMaterials();

        return (
            <>
                {/* Filtri */}
                {this.renderFilters()}

                {/* Lista Materiali */}
                <div className="d-flex flex-wrap" style={{ marginTop: "10px"}}>
                    {filteredMaterials.map((mat) => (
                        <MaterialeCard key={mat.id} id={mat.id} nome={mat.nome} onDelete={null} showDelete={false} />
                    ))}
                </div>
            </>
        );
    }
}

export default GestioneMaterialeWidget;

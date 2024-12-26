import React, { Component } from "react";
import axiosInstance from "../../config/axiosInstance";
import Button from "react-bootstrap/Button";

class GestioneMaterialeBase extends Component {
    constructor(props) {
        super(props);
        this.state = {
            filter: "ALL",
            materialList: [],
            loadingMaterial: false,
            materialError: null,
        };
    }

    componentDidMount() {
        this.loadMaterials();
    }

    loadMaterials = () => {
        this.setState({ loadingMaterial: true });
        axiosInstance
            .get("http://localhost:8080/api/terapeuta/materiale/getallbyterapeuta")
            .then((response) => {
                this.setState({ materialList: response.data, loadingMaterial: false });
            })
            .catch((error) => {
                this.setState({ materialError: "Errore nel caricamento dei materiali.", loadingMaterial: false });
            });
    };

    handleFilterChange = (filterType) => {
        this.setState({ filter: filterType });
    };

    getFilteredMaterials = () => {
        const { filter, materialList } = this.state;
        if (filter === "ALL") return materialList;
        return materialList.filter((mat) => mat.tipoMateriale === filter);
    };

    renderFilters() {
        const { filter } = this.state;
        return (
            <>
                <Button
                    variant={filter === "ALL" ? "btn-outline-primary btn-annulla-full" : "btn-outline-primary btn-annulla"}
                    onClick={() => this.handleFilterChange("ALL")}
                    className={"me-2"}
                >
                    ALL
                </Button>
                <Button
                    variant={filter === "PDF" ? "btn-outline-primary btn-annulla-full" : "btn-outline-primary btn-annulla"}
                    onClick={() => this.handleFilterChange("PDF")}
                    className={"me-2"}
                >
                    PDF
                </Button>
                <Button
                    variant={filter === "VIDEO" ? "btn-outline-primary btn-annulla-full" : "btn-outline-primary btn-annulla"}
                    onClick={() => this.handleFilterChange("VIDEO")}
                    className="me-2"
                >
                    Video
                </Button>
                <Button
                    variant={filter === "IMMAGINE" ? "btn-outline-primary btn-conferma-full" : "btn-outline-primary btn-conferma"}
                    onClick={() => this.handleFilterChange("IMMAGINE")}
                >
                    Immagine
                </Button>
            </>
        );
    }

    render() {
        return null; // Lascia il rendering ai componenti derivati
    }
}

export default GestioneMaterialeBase;

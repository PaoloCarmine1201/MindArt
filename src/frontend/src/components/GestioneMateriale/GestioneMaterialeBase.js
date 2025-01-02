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
            .get("http://localhost:8080/api/terapeuta/materiale/")
            .then((response) => {
                console.log('Dati ricevuti dall\'API:', response.data); // Log per debug
                // Verifica se response.data è un array
                if (Array.isArray(response.data)) {
                    this.setState({ materialList: response.data, loadingMaterial: false });
                }
                // Se response.data è un oggetto con una proprietà che contiene l'array
                else if (response.data && Array.isArray(response.data.data)) {
                    this.setState({ materialList: response.data.data, loadingMaterial: false });
                }
            })
            .catch((error) => {
                console.error('Errore nel caricamento dei materiali:', error);
                this.setState({ materialError: "Errore nel caricamento dei materiali.", loadingMaterial: false });
            });
    };



    handleFilterChange = (filterType) => {
        this.setState({ filter: filterType });
    };

    getFilteredMaterials = () => {
        const { filter, materialList = [] } = this.state; // Default a array vuoto
        if (filter === "ALL") return materialList;
        return materialList.filter((mat) => mat.tipoMateriale === filter);
    };

    renderFilters() {
        const { filter } = this.state;
        return (
            <>
                <Button
                    variant="btn-all"
                    className={`me-2 ${filter === "ALL" ? "btn-all btn-all-full" : "btn-all"}`}
                    onClick={() => this.handleFilterChange("ALL")}
                >
                    ALL
                </Button>
                <Button
                    variant="btn-pdf"
                    className={`me-2 ${filter === "PDF" ? "btn-pdf btn-pdf-full" : "btn-pdf"}`}
                    onClick={() => this.handleFilterChange("PDF")}
                >
                    PDF
                </Button>
                <Button
                    variant="btn-video"
                    className={`me-2 ${filter === "VIDEO" ? "btn-video btn-video-full" : "btn-video"}`}
                    onClick={() => this.handleFilterChange("VIDEO")}
                >
                    Video
                </Button>
                <Button
                    variant="btn-immagine"
                    className={`${filter === "IMMAGINE" ? "btn-immagine btn-immagine-full" : "btn-immagine"}`}
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

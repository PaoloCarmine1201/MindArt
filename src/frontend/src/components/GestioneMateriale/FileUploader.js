import React, { useState } from "react";
import axiosInstance from "../../config/axiosInstance";

function FileUploader({ onUpload }) {
    const [uploading, setUploading] = useState(false);

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            setUploading(true); // Indica che il caricamento è in corso
            const response = await axiosInstance.post(
                "http://localhost:8080/api/terapeuta/materiale",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            )

            if (response.status === 200) {
                onUpload(response.data);
            }
        } catch (error) {
            console.error("Errore durante il caricamento del file:", error);
        } finally {
            setUploading(false); // Fine del caricamento
            event.target.value = ""; // Resetta l'input file per consentire un nuovo upload
        }
    };

    return (
        <div>
            <h3>Carica Materiale</h3>
            <input
                type="file"
                onChange={handleFileChange}
                style={{ marginBottom: "10px" }}
                disabled={uploading} // Disabilita il pulsante durante il caricamento
            />
            {uploading && <p>Caricamento in corso...</p>}
        </div>
    );
}

export default FileUploader;

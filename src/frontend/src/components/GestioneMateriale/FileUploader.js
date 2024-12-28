import React, { useState } from "react";
import axiosInstance from "../../config/axiosInstance";
import '../../style/Button.css';

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
                "http://localhost:8080/api/terapeuta/materiale/",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );

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
        <div className="file-uploader">
            <label
                htmlFor="fileInput"
                className="custom-upload-button"
            >
                {uploading ? "Caricamento in corso..." : "Carica File"}
            </label>
            <input
                id="fileInput"
                type="file"
                onChange={handleFileChange}
                style={{ display: "none" }}
                disabled={uploading}
            />
        </div>
    );
}

export default FileUploader;

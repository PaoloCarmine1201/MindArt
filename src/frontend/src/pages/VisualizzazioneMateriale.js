import React, { useEffect, useState } from 'react';
import FileViewer from '../components/visualizzazioneMateriale/FileViewer';
import background from "../assets/ChildLoginBackground.jpg";
import { Stack } from "react-bootstrap";
import '../components/visualizzazioneMateriale/MiniVideoPlayer.css'; // Assicurati che questo sia il percorso corretto per il tuo CSS
import axiosInstance from "../config/axiosInstance";

/**
 * Componente per visualizzare il materiale del bambino.
 *
 * @returns {JSX.Element}
 */
const App = () => {
    const [materiale, setMateriale] = useState(null);
    const [error, setError] = useState(null);
    const [fileURL, setFileURL] = useState(null);

    // Effetto per recuperare il materiale al montaggio del componente
    useEffect(() => {
        const fetchMateriale = async () => {
            try {
                const response = await axiosInstance.get(`/api/bambino/sessione/getMateriale/`);
                setMateriale(response.data);
            } catch (err) {
                setError("Errore durante il caricamento del materiale.");
                console.error(err);
            }
        };

        if(materiale == null){
            fetchMateriale();
        }
    }, []); // Array di dipendenze vuoto per eseguire solo una volta

    // Effetto per creare e revocare l'URL del blob
    useEffect(() => {
        if (materiale && materiale.file && fileURL == null) {
            try {
                // Decodifica base64 in byte
                const byteCharacters = atob(materiale.file);
                const byteNumbers = Array.from(byteCharacters, char => char.charCodeAt(0));
                const byteArray = new Uint8Array(byteNumbers);

                // Crea un blob con il tipo corretto
                const blob = new Blob([byteArray], { type: materiale.tipoMateriale });
                const url = URL.createObjectURL(blob);
                setFileURL(url);

                // Revoca l'URL quando il componente si smonta o quando il file cambia
                return () => {
                    URL.revokeObjectURL(url);
                };
            } catch (error) {
                console.error("Errore nella decodifica del file:", error);
                setError("Errore nella decodifica del file.");
            }
        }
    }, [materiale]); // Dipende da 'materiale'

    if (error) return <div>{error}</div>;

    if (!materiale || !fileURL) return <div>Caricamento...</div>;

    return (
        <Stack
            className="d-flex justify-content-center align-items-center vh-100"
            style={{
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                backgroundImage: `url(${background})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
                zIndex: 1, // Imposta un valore positivo per assicurarti che sia visibile
            }}
        >
            <div className="content-wrapper" style={{ textAlign: 'center', color: '#fff' }}>
                <h1 className="app-heading">Visualizzatore di file</h1>

                <div className="mini-video-container">
                    <FileViewer
                        fileUrl={fileURL}
                        fileType={materiale.tipoMateriale}
                    />
                </div>
            </div>
        </Stack>
    );
};

export default App;

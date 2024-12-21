import React, { useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import TerminateModal from "../modal/TerminateModal";

const TerminaSessione = () => {
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [idSessione] = useState("");

    const handleLogout = async () => {
        setLoading(true);
        try {
            const response = await axios.patch(`/api/sessione/${idSessione}/termina`); // Chiamata all'API
            if (response.status === 200) {
                setMessage("Sessione terminata con successo.");
            }
        } catch (error) {
            if (error.response && error.response.status === 404) {
                setMessage("ID della sessione non trovato.");
            } else {
                setMessage("Errore nella terminazione della sessione.");
            }
        } finally {
            setLoading(false);
            setShowModal(false);
        }
    };

    const handleOpenModal = () => {
        setMessage(""); // Resetta il messaggio quando si apre il modale
        setShowModal(true);
    };

    return (
        <div className="container text-center mt-5">
            <button
                className="btn btn-danger"
                onClick={handleOpenModal}
            >
                Termina Sessione
            </button>

            {/*Modal di conferma*/}
            <TerminateModal
                show={showModal}
                onClose={() => setShowModal(false)}
                onConfirm={handleLogout}
                loading={loading}
            />

            {/*Messaggio di successo o errore*/}
            {message && (
                <div className={`alert mt-3 ${message.includes("successo") ? "alert-success" : "alert-danger"}`} role="alert">
                    {message}
                </div>
            )}
        </div>
    );
};

export default TerminaSessione;

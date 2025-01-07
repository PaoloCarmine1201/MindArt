import { useEffect, useState } from "react";
import axiosInstance from "../../config/axiosInstance";
import '../../style/ProfiloTerapeutaStyle.css';
import '../../style/Modal.css';
import { Link } from "react-router-dom";
import Modal from "react-bootstrap/Modal";
import {ModalBody, ModalFooter, ModalTitle} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {toast} from "react-toastify";

function ProfiloTerapuetaComponent() {
    const [terapeuta, setTerapeuta] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [confirmMessage, setConfirmMessage] = useState('');
    const [emailChanged, setEmailChanged] = useState(false);
    const [formData, setFormData] = useState({
        id: '',
        nome: '',
        cognome: '',
        email: ''
    });

    useEffect(() => {
        axiosInstance.get(`api/terapeuta/get`)
            .then(response => {
                setTerapeuta(response.data);
            })
            .catch(error => {
                console.error("Errore durante il recupero dei dati del terapeuta:", error);
            });
    }, []);

    useEffect(() => {
        if (terapeuta) {
            setFormData({
                id: terapeuta.id,
                nome: terapeuta.nome,
                cognome: terapeuta.cognome,
                dataDiNascita: terapeuta.dataDiNascita,
                email: terapeuta.email
            });
        }
    }, [terapeuta]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleClick = () => {
        console.log("Modifica profilo terapeuta");
        setShowModal(true);
    }

    const handleSubmit = () => {
        console.log(formData);
        axiosInstance.post(`api/terapeuta/update`, formData)
            .then(response => {
                console.log("Modifiche salvate con successo:", response.data);
                setShowModal(false);
                terapeuta.email !== formData.email ? setEmailChanged(true) : setEmailChanged(false);
                setConfirmMessage(emailChanged ? "Modifiche salvate con successo. E' necessario effettuare nuovamente il login." : "Modifiche salvate con successo.");
                setShowConfirmModal(true);
            })
            .catch(error => {
                console.error("Errore durante il salvataggio delle modifiche:", error);
                toast.error("Errore durante il salvataggio delle modifiche.");
            });
    };

    const handleChiudi = () => {
        setShowConfirmModal(false);
        if (emailChanged) {
            window.location.href = '/login';
        }else{
            window.location.reload();
        }
    }

    if (!terapeuta) {
        return <p>Caricamento in corso...</p>;
    }

    return (
        <div className="dettaglio-container">
            <Modal
                show={showModal}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <ModalBody>
                    <ModalTitle>Modifica i tuoi dati</ModalTitle>
                    {/*Form per modificare i dati del terapeuta*/}
                    <form>
                        <div className="form-group">
                            <label htmlFor="nome">Nome</label>
                            <input
                                type="text"
                                className="form-control"
                                id="nome"
                                name="nome"
                                value={formData.nome}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="cognome">Cognome</label>
                            <input
                                type="text"
                                className="form-control"
                                id="cognome"
                                name="cognome"
                                value={formData.cognome}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                            />
                        </div>
                    </form>
                </ModalBody>
                <ModalFooter>
                    <Button
                        onClick={() => setShowModal(false)}
                        className="btn-cancella"
                    >
                        Annulla
                    </Button>
                    <Button
                        type="button"
                        className="btn-conferma"
                        onClick={handleSubmit}
                    >
                        Salva
                    </Button>
                </ModalFooter>
            </Modal>
            <h2 className="dettaglio-header">{terapeuta.nome} {terapeuta.cognome}</h2>
            <div className="dettaglio-section">
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Data di Nascita:</label>
                    <span className="dettaglio-value">
                        {new Date(terapeuta.dataDiNascita).toLocaleDateString('it-IT')}
                    </span>
                </div>
                <div className="dettaglio-data-row">
                    <label className="dettaglio-label">Email:</label>
                    <span className="dettaglio-value">{terapeuta.email}</span>
                </div>
            </div>

            <div className="dettaglio-button-container text-end justify-content-end">
                {/* Pulsante Modifica */}
                <Button
                    onClick={handleClick}
                    className="btn-all"
                >
                    Modifica i tuoi dati
                </Button>
            </div>

            {/* Pulsante Indietro */}
            <Link to="/" className="dettaglio-button-link">
                <p className="dettaglio-back-button">↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default ProfiloTerapuetaComponent;

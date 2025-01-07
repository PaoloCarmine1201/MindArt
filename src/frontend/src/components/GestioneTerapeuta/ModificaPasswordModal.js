import {useEffect, useState} from "react";
import axiosInstance from "../../config/axiosInstance";
import Modal from "react-bootstrap/Modal";
import {ModalBody, ModalFooter, ModalTitle} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {toast} from "react-toastify";

function ModificaPasswordModal({show, onHide}) {
    const [terapeuta, setTerapeuta] = useState(null);
    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [passwordChanged, setPasswordChanged] = useState(false);
    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: ''
    });

    useEffect(() => {
        axiosInstance.get('api/terapeuta/get')
            .then(response => {
                setTerapeuta(response.data);
            })
            .catch(error => {
                console.error("Errore durante il recupero dei dati del terapeuta:", error);
            });
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPasswordData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleClose = () => {
        // Resetta i campi password
        setPasswordData({
            oldPassword: '',
            newPassword: '',
        });
        onHide(); // Chiama la funzione per chiudere il modale
    };

    const handleSubmit = async () => {

        if (!passwordData.oldPassword || !passwordData.newPassword) {
            toast.error("Compila tutti i campi.");
            return;
        }

        if (passwordData.newPassword === passwordData.oldPassword) {
            toast.error("La nuova password deve essere diversa dalla vecchia.");
            setPasswordData((prevState) => ({
                ...prevState,
                newPassword: '',
            }));
            return;
        }

        try {
            const response = await axiosInstance.post('api/terapeuta/cambia-password', {
                id: terapeuta.id,
                oldPassword: passwordData.oldPassword,
                newPassword: passwordData.newPassword,
            });

            if (response.status === 200 && response.data === "SUCCESS") {
                toast.success("Password modificata con successo.");
                setPasswordChanged(true);
                onHide();
                setShowConfirmModal(true);
            }
        } catch(error) {
            console.error("Errore durante il cambio password:", error);
            if (error.response && error.response.status === 400) {
                toast.error("Vecchia password errata.");
                setPasswordData((prevState) => ({
                    ...prevState,
                    oldPassword: '',
                }));
            }
            else {
                toast.error("Errore durante il cambio della password.");
            }
        }
    };

    const handleConfirmClose = () => {
        setShowConfirmModal(false);
        if (passwordChanged) {
            window.location.href = '/login';
        }
    };

    return (
        <>

            <Modal
                show={showConfirmModal}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <ModalBody>
                    <ModalTitle>Password Modificata</ModalTitle>
                    <p>{passwordChanged ? "Reinserisci le credenziali per continuare." : "Operazione annullata."}</p>
                    <ModalFooter>
                        <Button
                            onClick={handleConfirmClose}
                            className="btn-conferma btn-conferma-in"
                        >
                            {passwordChanged ? "Effettua il login" : "Chiudi"}
                        </Button>
                    </ModalFooter>
                </ModalBody>
            </Modal>
            {/* Modal di modifica */}
            <Modal
                show={show}
                onHide={onHide}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <ModalBody>
                    <ModalTitle>Modifica la tua password</ModalTitle>
                    <form>
                        <div className="form-group">
                            <label htmlFor="oldPassword">Vecchia Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="oldPassword"
                                name="oldPassword"
                                value={passwordData.oldPassword}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="newPassword">Nuova Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="newPassword"
                                name="newPassword"
                                value={passwordData.newPassword}
                                onChange={handleChange}
                            />
                        </div>
                    </form>
                </ModalBody>
                <ModalFooter>
                    <Button
                        onClick={handleClose}
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
        </>
    );
}

export default ModificaPasswordModal;

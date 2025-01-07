import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import axiosInstance from "../../config/axiosInstance";
import {Button, Modal} from "react-bootstrap";
import "../../style/Modal.css"

const ConfermaDisegno = ({nomeBottone = "Consegna", disegno = null}) => {
    const [showModal, setShowModal] = useState(false);

    // All'interno del tuo componente BoardDisegno.jsx

    const dataURLtoBlob = (dataurl) => {
        const arr = dataurl.split(',');
        const mimeMatch = arr[0].match(/:(.*?);/);
        if (!mimeMatch) {
            throw new Error('Invalid data URL');
        }
        const mime = mimeMatch[1];
        const bstr = atob(arr[1]);
        let n = bstr.length;
        const u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], { type: mime });
    };

    const handleSubmit = async () => {
        const formData = new FormData();


        try {
            if(disegno.current){

                const dataURL = disegno.current.toDataURL({
                    pixelRatio: 3,
                    mimeType: 'image/jpeg',
                    quality: 0.8
                });

                const imageBlob = dataURLtoBlob(dataURL);
                formData.append('image', imageBlob, 'disegno.jpeg');
            }

            const response = await axiosInstance.post(`/api/bambino/sessione/consegna`,formData, disegno,{
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            if (response && response.status === 200) {
                console.log("Disegno consegnato con successo.");
                //window.location.href = "/childlogin";
                //localStorage.removeItem("jwtToken");
            }
        } catch (error) {
            if (error.response && error.response.status === 404) {
                console.log("ID della sessione non trovato.");
            } else if (error.response && error.response.status === 403){
                console.log("Errore nella consegna del disegno.");
            } else {
                console.error("Errore: " + error);
            }

        } finally {
            setShowModal(false);
        }
    };

    const handleOpenModal = () => {
        setShowModal(true);
    };

    return (
        <div className="">
            <Button className="btn-conferma consegna" onClick={handleOpenModal}>
                {nomeBottone}
            </Button>

            {/* Modale di conferma */}
            <Modal
                show={showModal}
                backdropClassName="custom-backdrop"
                keyboard={false}
                aria-labelledby="contained-modal-title-vcenter"
                centered
                dialogClassName="custom-modal"
            >
                <Modal.Header>
                    <Modal.Title>
                        Sei sicuro di voler consegnare?
                    </Modal.Title>
                </Modal.Header>
                <Modal.Footer>
                    <Button
                        className="btn-cancella"
                        onClick={() => {
                            setShowModal(false)
                        }}
                    >
                        Annulla
                    </Button>
                    <Button
                        className="btn-conferma"
                        onClick={handleSubmit}
                    >
                        Conferma
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default ConfermaDisegno;
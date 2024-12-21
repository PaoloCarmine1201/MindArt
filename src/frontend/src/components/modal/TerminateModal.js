import React from 'react';

const TerminateModal = ({ show, onClose, onConfirm, loading }) => {
    return (
        <div
            className={`modal fade ${show ? 'show' : ''}`}
            style={{ display: show ? 'block' : 'none' }}
            tabIndex="-1"
            aria-labelledby="modalLabel"
            aria-hidden={!show}
        >
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-body text-center">
                        <p>Sei sicuro di voler terminare la sessione?</p>
                    </div>
                    <div className="modal-footer justify-content-center">
                        <button
                            className="btn btn-secondary"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Annulla
                        </button>
                        <button
                            className="btn"
                            style={{
                                backgroundColor: '#001447',
                                color: '#fff',
                            }}
                            onClick={onConfirm}
                            disabled={loading}
                        >
                            {loading ? "Caricamento..." : "Conferma"}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TerminateModal;
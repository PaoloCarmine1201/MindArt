import React from 'react';

const ToastNotification = ({ show, message, type }) => {
    return (
        <div className={`toast align-items-center text-white ${show ? 'show' : ''} bg-${type} position-fixed top-0 end-0 p-3`} style={{ zIndex: 1050 }}>
            <div className="d-flex">
                <div className="toast-body">
                    {message}
                </div>
            </div>
        </div>
    );
};

export default ToastNotification;

// SelezioneBambino.jsx
import React from 'react';
import { Form, Spinner } from 'react-bootstrap';

const SelezioneBambino = ({ childrenList, loading, error, selectedChildren, setFieldValue }) => {
    const handleCheckboxChange = (e, bambinoId) => {
        if (e.target.checked) {
            setFieldValue('bambini', [...selectedChildren, bambinoId]);
        } else {
            setFieldValue('bambini', selectedChildren.filter(id => id !== bambinoId));
        }
    };

    if (loading) {
        return (
            <div className="d-flex align-items-center">
                <Spinner animation="border" size="sm" className="me-2" />
                Caricamento...
            </div>
        );
    }

    if (error) {
        return <div className="text-danger">{error}</div>;
    }

    if (childrenList.length === 0) {
        return <div>Nessun bambino disponibile per la selezione.</div>;
    }

    return (
        <>
            {childrenList.map(bambino => (
                <Form.Check
                    key={bambino.id}
                    type="checkbox"
                    label={bambino.nome}
                    name="bambini"
                    value={bambino.id}
                    checked={selectedChildren.includes(bambino.id)}
                    onChange={(e) => handleCheckboxChange(e, bambino.id)}
                />
            ))}
        </>
    );
};

export default SelezioneBambino;

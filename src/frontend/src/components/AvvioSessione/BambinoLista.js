import React from 'react';
import { Card, ListGroup, Spinner, Form } from 'react-bootstrap';

import '../../style/CheckBox.css';

const BambinoLista = ({
                          childrenList,
                          loading,
                          error,
                          selectedChildren,
                          setFieldValue,
                          isInvalid = false
}) => {
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
        <div className={isInvalid ? 'is-invalid' : ''}>
            <Card className="shadow-sm">
                <Card.Header style={{ backgroundColor: '#f8f9fa', color: '#000', fontSize: '1.3rem' }}>Seleziona partecipanti</Card.Header>
                <ListGroup variant="flush">
                    {childrenList.map(bambino => (
                        <ListGroup.Item key={bambino.id} className="d-flex align-items-center">
                            <Form.Check
                                type="checkbox"
                                label={bambino.nome + ' ' + bambino.cognome}
                                name="bambini"
                                value={bambino.id}
                                checked={selectedChildren.includes(bambino.id)}
                                onChange={(e) => handleCheckboxChange(e, bambino.id)}
                                className="me-3"
                            />
                            <div className="ms-auto">
                                {/* Opzionale: Aggiungi ulteriori informazioni del bambino */}
                                <small className="text-muted">ID: {bambino.id}</small>
                            </div>
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            </Card>
        </div>
    );
};

export default BambinoLista;
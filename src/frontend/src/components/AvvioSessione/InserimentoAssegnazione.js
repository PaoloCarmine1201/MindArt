import { FloatingLabel, Form, FormControl, FormGroup } from "react-bootstrap";
import { useFormikContext } from "formik";
import React from "react";

function InserimentoAssegnazione() {
    const { values, errors, touched, handleChange, handleBlur } = useFormikContext();

    return (
        <Form.Group className="mb-3">
            <label style={{
                fontSize: '1.3rem', // Larger font size
                marginBottom: '1rem', // Space below the label
                display: 'block', // Ensures the label spans the entire width
                color: '#333' // Neutral text color
            }}>
                Inserisci un tema per questo disegno:
            </label>
            {/* Floating Label for enhanced input styling */}
            <FloatingLabel
                controlId="temaAssegnato"
                label="Tema assegnato"
                className="mb-3"
            >
                <Form.Control
                    name="temaAssegnato"
                    type="text"
                    placeholder="Es. peppino"
                    onChange={handleChange}
                    onBlur={handleBlur}
                    value={values.temaAssegnato}
                    isInvalid={touched.temaAssegnato && !!errors.temaAssegnato}
                    style={{borderRadius: "8px"}}
                />
                <Form.Control.Feedback type="invalid" tooltip>
                    {typeof errors.temaAssegnato === 'string' ? errors.temaAssegnato : ''}
                </Form.Control.Feedback>
            </FloatingLabel>
        </Form.Group>
    );
}

export default InserimentoAssegnazione;

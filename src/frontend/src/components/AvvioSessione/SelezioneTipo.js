import React from 'react';
import { Form, FormGroup, FormLabel } from 'react-bootstrap';
import { useFormikContext } from 'formik';

// Importa il CSS di Bootstrap
import "bootstrap/dist/css/bootstrap.min.css";

function SelezioneTipo() {
    const { values, errors, touched, handleChange } = useFormikContext();

    return (
        <FormGroup>
            <FormLabel>Tipo sessione</FormLabel>
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="attività"
                value="attività"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
            >
            </Form.Check>
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="apprendimento"
                value="apprendimento"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange} >
            </Form.Check>
            <Form.Control.Feedback type="invalid">{errors.tipoSessione}</Form.Control.Feedback>
        </FormGroup>
    );
}

export default SelezioneTipo;
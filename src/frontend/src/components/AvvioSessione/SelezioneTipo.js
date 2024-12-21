// Step1TipoSessione.jsx
import React from 'react';
import { FormGroup, Form, FormLabel } from 'react-bootstrap';
import { useFormikContext } from 'formik';

const SelezioneTipo = () => {
    const { values, errors, touched, handleChange } = useFormikContext();

    return (
        <FormGroup>
            <label>Tipo sessione</label>
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="attività"
                value="attività"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
                checked={values.tipoSessione === 'attività'}
            />
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="apprendimento"
                value="apprendimento"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
                checked={values.tipoSessione === 'apprendimento'}
            />
            <Form.Control.Feedback type="invalid">
                {typeof errors.tipoSessione === 'string' ? errors.tipoSessione : ''}
            </Form.Control.Feedback>
        </FormGroup>
    );
};

export default SelezioneTipo;

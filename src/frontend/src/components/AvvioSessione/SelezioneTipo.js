// Step1TipoSessione.jsx
import React from 'react';
import { FormGroup, Form } from 'react-bootstrap';
import { useFormikContext } from 'formik';
import '../../style/Radio.css'

const SelezioneTipo = () => {
    const { values, errors, touched, handleChange } = useFormikContext();

    return (
        <FormGroup>
            <label style={{
                fontSize: '1.3rem', // Larger font size
                marginBottom: '1rem', // Space below the label
                display: 'block', // Ensures the label spans the entire width
                color: '#333' // Neutral text color
            }}>
                Seleziona tipo sessione:
            </label>


            <Form.Check
                type="radio"
                name="tipoSessione"
                label="Disegno"
                value="DISEGNO"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
                checked={values.tipoSessione === 'DISEGNO'}
                className='custom-radio'
            />
            <hr className='radio-divider'/>
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="Colore"
                value="COLORE"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
                checked={values.tipoSessione === 'COLORE'}
                className='custom-radio'
            />
            <hr className='radio-divider'/>
            <Form.Check
                type="radio"
                name="tipoSessione"
                label="Apprendimento"
                value="APPRENDIMENTO"
                isInvalid={touched.tipoSessione && !!errors.tipoSessione}
                onChange={handleChange}
                checked={values.tipoSessione === 'APPRENDIMENTO'}
                className='custom-radio'
            />
            <hr className='radio-divider'/>
            <Form.Control.Feedback type="invalid">
                {typeof errors.tipoSessione === 'string' ? errors.tipoSessione : ''}
            </Form.Control.Feedback>
        </FormGroup>
    );
};

export default SelezioneTipo;

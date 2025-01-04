// Step3SelezioneBambino.jsx
import React from 'react';
import { FormGroup, Form} from 'react-bootstrap';
import { useFormikContext } from 'formik';
import BambinoLista from './BambinoLista';

const SelezioneBambino = ({ childrenList, loading, error }) => {
    const { values, setFieldValue, errors } = useFormikContext();

    return (
        <FormGroup>
            <BambinoLista
                childrenList={childrenList}
                loading={loading}
                error={error}
                selectedChildren={values.bambini}
                setFieldValue={setFieldValue}
            />
            <Form.Control.Feedback type="invalid">
                {typeof errors.bambini === 'string'? errors.bambini : ''}
            </Form.Control.Feedback>
        </FormGroup>
    );
};

export default SelezioneBambino;

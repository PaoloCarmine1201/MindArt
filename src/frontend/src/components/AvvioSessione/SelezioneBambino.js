// Step3SelezioneBambino.jsx
import React from 'react';
import { FormGroup, FormLabel, Form} from 'react-bootstrap';
import { useFormikContext } from 'formik';
import BambinoLista from './BambinoLista';

const SelezioneBambino = ({ childrenList, loading, error }) => {
    const { values, setFieldValue, errors, touched } = useFormikContext();

    return (
        <FormGroup>
            <label>Scegli i bambini</label>
            <BambinoLista
                childrenList={childrenList}
                loading={loading}
                error={error}
                selectedChildren={values.bambino}
                setFieldValue={setFieldValue}
            />
            <Form.Control.Feedback type="invalid">
                {typeof errors.bambino === 'string'? errors.bambino : ''}
            </Form.Control.Feedback>
        </FormGroup>
    );
};

export default SelezioneBambino;

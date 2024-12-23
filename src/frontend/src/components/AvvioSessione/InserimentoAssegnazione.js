import {FloatingLabel, Form, FormControl, FormGroup} from "react-bootstrap";
import {useFormikContext} from "formik";

function InserimentoAssegnazione() {
    const { values, errors, touched, handleChange, handleBlur } = useFormikContext();

    return (
        <Form.Group>
            <Form.Control
                name="temaAssegnato"
                type="text"
                placeholder="Es. peppino"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.temaAssegnato}
                isInvalid={touched.temaAssegnato && !!errors.temaAssegnato}
            />
            <Form.Control.Feedback type="invalid" tooltip>
                {typeof errors.nome === 'string' ? errors.nome : ''}
            </Form.Control.Feedback>
        </Form.Group>
    );
}

export default InserimentoAssegnazione;
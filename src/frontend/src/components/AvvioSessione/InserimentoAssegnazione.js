import {FloatingLabel, Form, FormControl, FormGroup} from "react-bootstrap";
import {useFormikContext} from "formik";

function InserimentoAssegnazione() {
    const { values, errors, touched, handleChange, handleBlur } = useFormikContext();

    return (
        <Form.Group>
            <Form.Control
                name="assegnazione"
                type="text"
                placeholder="Es. peppino"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.assegnazione}
                isInvalid={touched.assegnazione && !!errors.assegnazione}
            />
            <Form.Control.Feedback type="invalid" tooltip>
                {typeof errors.nome === 'string' ? errors.nome : ''}
            </Form.Control.Feedback>
        </Form.Group>
    );
}

export default InserimentoAssegnazione;
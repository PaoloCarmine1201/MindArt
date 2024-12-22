import {Form, FormCheck, FormGroup} from "react-bootstrap";
import React from "react";
import {useFormikContext} from "formik";

function SelezioneMateriale({materials, setMaterials}) {
    const { values, setFieldValue, errors, touched } = useFormikContext();

    const materiale = [1, 2, 3, 4, 5, 6]

    const handleCheckboxChange = (mat) => {
        setFieldValue('materiale', mat);
    };

    return (
        <FormGroup>
            {materiale.map((mat) => (
                <Form.Check
                    name="materiale"
                    type="radio"
                    label={mat}
                    value={values.materiale}
                    checked={values.materiale === mat}
                    onChange={() => handleCheckboxChange(mat)}
                />
            ))}
        </FormGroup>
    );
}

export default SelezioneMateriale;
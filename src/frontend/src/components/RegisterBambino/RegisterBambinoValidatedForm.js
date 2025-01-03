import * as formik from "formik";
import * as yup from "yup";
import '../../style/RegisterBambino.css';
import RegisterBambinoForm from "./RegisterBambinoForm";


function RegisterBambinoValidatedForm({handleSubmit , formRef}) {

    const { Formik } = formik;

    // Validation schema
    const schema = yup.object().shape({
        nome: yup
            .string()
            .matches(/^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]{2,50}$/, 'formato nome non valido')
            .required('campo nome richiesto'),
        cognome: yup
            .string()
            .matches(/^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]{2,50}$/, 'formato cognome non valido')
            .required('campo cognome richiesto'),
        sesso: yup
            .string()
            .oneOf(['MASCHIO', 'FEMMINA'], 'formato sesso non valido')
            .required('campo sesso richiesto'),
        dataDiNascita: yup
            .date()
            .max(new Date(), 'la data di nascita deve essere nel passato')
            .required('data di nascita richiesta'),
        codiceFiscale: yup
            .string()
            .matches(/^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$/i, 'formato codice fiscale non valido')
            .required('codice fiscale richiesto'),
        emailGenitore: yup
            .string()
            .email('formato email non valido')
            .required('email tutore richiesta'),
        telefonoGenitore: yup
            .string()
            .matches(/^\+?[0-9\s-]{8,15}$/, 'telefono tutore non valido')
            .required('telefono tutore richiesto')
    });

    return (
        // Formik component to handle the form state and validation
        <Formik
            innerRef={formRef}
            validationSchema={schema}
            onSubmit={handleSubmit}
            validateOnBlur={true}
            validateOnChange={true}
            initialValues={{
                nome: '',
                cognome: '',
                sesso: 'MASCHIO',
                dataDiNascita: '',
                codiceFiscale: '',
                emailGenitore: '',
                telefonoGenitore: ''
            }}
        >
            {/* Form component with the form fields */}
            {({handleSubmit, handleChange, handleBlur, values, errors, touched}) => (
                <RegisterBambinoForm
                    handleSubmit={handleSubmit}
                    handleChange={handleChange}
                    handleBlur={handleBlur}
                    values={values}
                    errors={errors}
                    touched={touched}
                />
            )}
        </Formik>
    );
}

export default RegisterBambinoValidatedForm;
import * as formik from "formik";
import * as yup from "yup";
import '../../style/RegisterBambino.css';
import InformazioniBambinoForm from "./InformazioniBambinoForm";


function InformazioniBambinoValidatedForm({handleSubmit , formRef, bambino = null}) {

    const { Formik } = formik;
    let initialValues;

    if(bambino === null){
        initialValues = {
            nome: '',
            cognome: '',
            sesso: 'MASCHIO',
            dataDiNascita: '',
            codiceFiscale: '',
            emailGenitore: '',
            telefonoGenitore: ''
        }
    }else{
        initialValues = {
            nome: bambino.nome,
            cognome: bambino.cognome,
            sesso: bambino.sesso,
            dataDiNascita: bambino.dataDiNascita,
            codiceFiscale: bambino.codiceFiscale,
            emailGenitore: bambino.emailGenitore,
            telefonoGenitore: bambino.telefonoGenitore
        }
    }

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
            .matches(/^(\+\d{1,2}\s?)?1?-?\.?\s?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/, 'telefono tutore non valido')
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
            initialValues={initialValues}
        >
            {/* Form component with the form fields */}
            {({handleSubmit, handleChange, handleBlur, values, errors, touched}) => (
                <InformazioniBambinoForm
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

export default InformazioniBambinoValidatedForm;
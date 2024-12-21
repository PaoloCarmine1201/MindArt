import * as yup from 'yup';

export const stepOneSchema = yup.object().shape({
    tipoSessione: yup
        .string()
        .required('Il tipo è obbligatorio')
        .matches(/^(attività|apprendimento)$/, 'Il tipo deve essere attività o apprendimento')
});

export const stepTwoSchema = yup.object().shape({
    materiale: yup
        .string()
        .required('Il materiale è obbligatorio')
    //todo regex path materiale
});

export const stepThreeSchema = yup.object().shape({
    bambino: yup
        .array()
        .min(1, 'Devi selezionare almeno un bambino')
        .required('Requisito')
});

import * as yup from 'yup';

export const stepOneSchema = yup.object().shape({
    tipoSessione: yup
        .string()
        .required('Il tipo è obbligatorio')
        .matches(/^(DISEGNO|COLORE|APPRENDIMENTO)$/, 'Il tipo deve essere attività o apprendimento')
});

export const stepTwoSchema = yup.object().shape({
    materiale: yup
        .string()
        .required('Il materiale è obbligatorio')
    //todo regex path materiale
});

export const stepThreeSchemaMult = yup.object().shape({
    bambini: yup
        .array()
        .min(1, 'Devi selezionare almeno un bambino')
        .required('Requisito')
});

export const stepThreeSchemaSingle = yup.object().shape({
    bambini: yup
        .array()
        .min(1, 'Devi selezionare almeno un bambino')
        .max(1, 'Puoi selezionare al massimo un bambino')
        .required('Requisito')
});

export const stepFourSchema = yup.object().shape({
    temaAssegnato: yup
        .string()
        .required('L\'assegnazione è obbligatoria')
});

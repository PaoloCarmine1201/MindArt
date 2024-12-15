import * as Yup from 'yup';

export const stepOneSchema = Yup.object().shape({
    tipoSessione: Yup.string().required('Il tipo è obbligatorio').matches(/(attività|apprendimento)/, 'Il tipo deve essere attività o apprendimento'),
});

export const stepTwoSchema = Yup.object().shape({
    materiale: Yup.string().required('Il materiale è obbligatorio') //todo regex path materiale
});

export const stepThreeSchema = Yup.object().shape({
    bambino: Yup.string().required('Il bambino è obbligatorio') //todo regex list bambini
});

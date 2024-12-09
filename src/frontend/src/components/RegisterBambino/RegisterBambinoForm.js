// BambinoFormFields.js
import { Col, FloatingLabel, Form, Row } from 'react-bootstrap';

function RegisterBambinoForm({
                               handleChange,
                               handleBlur,
                               values,
                               errors,
                               touched,
                           }) {
    return (
        <Form id="registerBambinoForm">
            {/* Nome */}
            <Form.Group controlId="registerBambino.nome">
                <FloatingLabel controlId="registerBambino.nome" label="Nome">
                    <Form.Control
                        name="nome"
                        type="text"
                        placeholder="Es. peppino"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.nome}
                        isInvalid={touched.nome && !!errors.nome}
                    />
                    <Form.Control.Feedback type="invalid" tooltip>
                        {typeof errors.nome === 'string' ? errors.nome : ''}
                    </Form.Control.Feedback>
                </FloatingLabel>
            </Form.Group>

            {/* Cognome */}
            <Form.Group controlId="registerBambino.cognome">
                <FloatingLabel controlId="registerBambino.cognome" label="Cognome">
                    <Form.Control
                        name="cognome"
                        type="text"
                        placeholder="Es. rossi"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.cognome}
                        isInvalid={touched.cognome && !!errors.cognome}
                    />
                    <Form.Control.Feedback type="invalid" tooltip>
                        {typeof errors.cognome === 'string' ? errors.cognome : ''}
                    </Form.Control.Feedback>
                </FloatingLabel>
            </Form.Group>

            <Row>
                <Col>
                    {/* Sesso */}
                    <Form.Group controlId="registerBambino.sesso">
                        <FloatingLabel controlId="registerBambino.sesso" label="Sesso">
                            <Form.Select
                                name="sesso"
                                aria-label="Sesso:"
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.sesso}
                                isInvalid={touched.sesso && !!errors.sesso}
                            >
                                <option value="MASCHIO">Maschio</option>
                                <option value="FEMMINA">Femmina</option>
                            </Form.Select>
                            <Form.Control.Feedback type="invalid" tooltip>
                                {typeof errors.sesso === 'string' ? errors.sesso : ''}
                            </Form.Control.Feedback>
                        </FloatingLabel>
                    </Form.Group>
                </Col>
                <Col>
                    {/* Data di nascita */}
                    <Form.Group controlId="registerBambino.dataDiNascita">
                        <FloatingLabel controlId="registerBambino.dataDiNascita" label="Data di nascita">
                            <Form.Control
                                name="dataDiNascita"
                                type="date"
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.dataDiNascita}
                                isInvalid={touched.dataDiNascita && !!errors.dataDiNascita}
                            />
                            <Form.Control.Feedback type="invalid" tooltip>
                                {typeof errors.dataDiNascita === 'string' ? errors.dataDiNascita : ''}
                            </Form.Control.Feedback>
                        </FloatingLabel>
                    </Form.Group>
                </Col>
            </Row>

            {/* Codice fiscale */}
            <Form.Group controlId="registerBambino.codiceFiscale">
                <FloatingLabel controlId="registerBambino.codiceFiscale" label="Codice fiscale">
                    <Form.Control
                        name="codiceFiscale"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        type="text"
                        placeholder="Es. ABCDEF12G34H567I"
                        value={values.codiceFiscale}
                        isInvalid={touched.codiceFiscale && !!errors.codiceFiscale}
                    />
                    <Form.Control.Feedback type="invalid" tooltip>
                        {typeof errors.codiceFiscale === 'string' ? errors.codiceFiscale : ''}
                    </Form.Control.Feedback>
                </FloatingLabel>
            </Form.Group>

            {/* Email tutore */}
            <Form.Group controlId="registerBambino.emailGenitore">
                <FloatingLabel controlId="registerBambino.emailGenitore" label="Email tutore">
                    <Form.Control
                        name="emailGenitore"
                        type="email"
                        placeholder="Es. genitore@example.com"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.emailGenitore}
                        isInvalid={touched.emailGenitore && !!errors.emailGenitore}
                    />
                    <Form.Control.Feedback type="invalid" tooltip>
                        {typeof errors.emailGenitore === 'string' ? errors.emailGenitore : ''}
                    </Form.Control.Feedback>
                </FloatingLabel>
            </Form.Group>

            {/* Telefono tutore */}
            <Form.Group controlId="registerBambino.telefonoGenitore">
                <FloatingLabel controlId="registerBambino.telefonoGenitore" className="last-field" label="Numero telefono tutore">
                    <Form.Control
                        name="telefonoGenitore"
                        type="text"
                        placeholder="Es. +39123456789"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.telefonoGenitore}
                        isInvalid={touched.telefonoGenitore && !!errors.telefonoGenitore}
                    />
                    <Form.Control.Feedback type="invalid" tooltip>
                        {typeof errors.telefonoGenitore === 'string' ? errors.telefonoGenitore : ''}
                    </Form.Control.Feedback>
                </FloatingLabel>
            </Form.Group>
        </Form>
    );
}

export default RegisterBambinoForm;

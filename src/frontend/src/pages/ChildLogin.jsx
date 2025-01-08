import React from 'react';
import { Form, Button, Container, FloatingLabel } from 'react-bootstrap';
import { Formik } from 'formik';
import * as Yup from 'yup';
import 'bootstrap/dist/css/bootstrap.min.css';
import logo from '../assets/logo_vertical_1024x1024.png';
import background from '../assets/ChildLoginBackground.jpg';
import {toast} from "react-toastify";
import axiosInstance from "../config/axiosInstance";
import { useNavigate} from 'react-router-dom';
import {useAuth} from "../auth/AuthProvider";


// Component definition for the child login form
function ChildLogin() {
    const { login } = useAuth();
    const navigate = useNavigate(); // For navigation
    // Validation schema using Yup
    const validationSchema = Yup.object().shape({
        code: Yup.string()
            .required("Per favore, inserisci il codice") // Field is required
            .matches(/^\w{6}$/, "Formato del codice non valido"), // Must be exactly 6 alphanumeric characters
    });

/*    useEffect(async () => {
        const jwtToken = localStorage.getItem('jwtToken');
        if(jwtToken){
            const sessione = await axiosInstance.get('/api/bambino/sessione/');

            if(sessione.data === ""){
                throw Error("No session found");
            }
            else{
                switch (sessione.data.tipoSessione) {
                    case "DISEGNO":
                        navigate('/child/draw');
                        break;
                    case "COLORE":
                        navigate('/child/colore');
                        break;
                    case "APPRENDIMENTO":
                        navigate('/visualizzazioneMateriale');
                    default:
                        throw Error("Invalid session type");
                }
            }
        }
    }, []);*/

    // Submit handler for the form
    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            // Prepare the payload
            const payload = { codice: values.code };

            // Make a POST request to /api/bambino/login using axiosInstance
            const response = await axiosInstance.post('/auth/bambino/login', payload);

            // Handle successful login
            console.log("Login successful:", response.data);

            login(response.data);

            const sessione = await axiosInstance.get('/api/bambino/sessione/');
            console.log(sessione.data.tipoSessione)
            if(sessione.data === ""){
                throw Error("No session found");
            }
            else{
                switch (sessione.data.tipoSessione) {
                    case "DISEGNO":
                        navigate('/child/draw');
                        break;
                    case "COLORE":
                        navigate('/child/colore');
                        break;
                    case "APPRENDIMENTO":
                        navigate('/visualizzazioneMateriale');
                        break;
                    default:
                        throw Error("Invalid session type");
                }
            }

        } catch (error) {
            console.error("Errore durante il login:", error);

            // Check if the error is from the server and contains a message
            if (error.response && error.response.data && error.response.data.message) {
                toast.error(error.response.data.message, {
                    position: 'bottom-right',
                });
            } else {
                // Generic error message
                toast.error('Si è verificato un errore durante il login. Riprova più tardi.', {
                    position: 'bottom-right',
                });
            }
        } finally {
            setSubmitting(false); // Indicate that the submission has completed
        }
    };

    return (
        // Outer container for the page layout, centered both vertically and horizontally
        <Container
            fluid
            className="d-flex flex-column align-items-center justify-content-center vh-100"
            style={{
                position: 'relative',
                overflow: 'hidden',
            }}
        >
            {/* Background layer */}
            <div
                style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundImage: `url(${background})`, // Set the background image
                    backgroundSize: 'cover', // Scale the image to cover the entire area
                    backgroundPosition: 'center', // Center the image
                    backgroundAttachment: 'fixed', // Ensure background stays fixed during scrolling
                    zIndex: '-2', // Place the background behind other content
                }}
            />

            {/* Main form container */}
            <Container
                className="d-flex flex-column align-items-center justify-content-center p-4 shadow"
                style={{
                    border: '3px solid #001447', // Blue border
                    backgroundColor: 'rgba(255, 255, 255)', // White background
                    zIndex: '1', // Place content above the background
                    maxWidth: '500px', // Limit the container width
                    borderRadius: '10px', // Rounded corners
                }}
            >
                {/* Application logo */}
                <img
                    src={logo}
                    alt="Application logo"
                    className="img-fluid mb-4"
                    style={{
                        width: '100px', // Set logo size
                        zIndex: '10',
                    }}
                />
                <h1 className="text-center mb-4">Partecipa alla sessione!</h1> {/* Title of the form */}

                {/* Formik for form handling with validation */}
                <Formik
                    initialValues={{ code: '' }} // Initial value for the code input
                    validationSchema={validationSchema} // Validation rules defined above
                    onSubmit={handleSubmit} // Submit handler
                    validateOnChange={false} // Disable validation on value change
                    validateOnBlur={false} // Disable validation on field blur
                >
                    {({
                          values, // Current form values
                          errors, // Validation errors
                          touched, // Tracks whether a field has been touched
                          handleChange, // Change handler for inputs
                          handleSubmit, // Submit handler
                          isSubmitting, // Tracks whether the form is being submitted
                      }) => (
                        <Form className="w-100 mx-auto" onSubmit={handleSubmit}>
                            {/* Code input field */}
                            <Form.Group className="mb-3 text-center m-bot m-2">
                                <FloatingLabel label={'Codice'}>
                                    <Form.Control
                                        type="text"
                                        name="code"
                                        placeholder="Enter your code..."
                                        value={values.code} // Controlled component
                                        onChange={handleChange} // Update form state on change
                                        onBlur={() => {}} // Disable validation on blur
                                        isInvalid={!!errors.code && touched.code} // Show error state
                                        className="text-center"
                                        style={{
                                            fontSize: '1.5em', // Larger font size for better readability
                                            borderColor: '#001447', // Custom border color
                                            borderRadius: '10px', // Rounded corners
                                        }}
                                    />
                                    {/* Feedback message for validation errors */}
                                    <Form.Control.Feedback
                                        type="invalid"
                                        style={{ marginTop: '0.5rem', position: 'static' }}
                                    >
                                        {errors.code} {/* Display validation error */}
                                    </Form.Control.Feedback>
                                </FloatingLabel>
                            </Form.Group>

                            {/* Submit button */}
                            <Form.Group className="d-flex justify-content-center">
                                <Button
                                    type="submit"
                                    disabled={isSubmitting} // Disable button during submission
                                    style={{
                                        backgroundColor: '#001447', // Blue background
                                        borderColor: '#001447', // Blue border
                                        borderRadius: '10px', // Rounded corners
                                        fontSize: '1.2em', // Slightly larger font size
                                        width: '70%', // Make the button smaller than the input field
                                        maxWidth: '300px', // Maximum width for the button
                                    }}
                                >
                                    Partecipa
                                </Button>
                            </Form.Group>
                        </Form>
                    )}
                </Formik>
            </Container>
        </Container>
    );
}

export default ChildLogin; // Export the component for use in other parts of the app
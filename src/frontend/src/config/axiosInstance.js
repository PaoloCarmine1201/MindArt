import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080", // URL base del tuo server backend
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("jwtToken"); // Recupera il token JWT da localStorage

        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`; // Aggiungi il token all'intestazione Authorization
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;

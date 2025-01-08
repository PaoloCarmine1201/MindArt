import {useAuth} from "./AuthProvider";
import {Navigate} from "react-router-dom";
import React from "react";

function ProtectedRouteChild({ children }) {
    const { isAuthenticated } = useAuth();

    return isAuthenticated ? children : <Navigate to="/childlogin" />;
}

export default ProtectedRouteChild;
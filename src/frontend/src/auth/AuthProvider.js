import React, { createContext, useState, useContext } from "react";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem("jwtToken") || null);

    const login = (newToken) => {
        setToken(newToken);
        localStorage.setItem("jwtToken", newToken);
    };

    const logout = () => {
        setToken(null);
        localStorage.removeItem("jwtToken");
    };

    const isAuthenticated = Boolean(token);

    return (
        <AuthContext.Provider value={{ isAuthenticated, token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}

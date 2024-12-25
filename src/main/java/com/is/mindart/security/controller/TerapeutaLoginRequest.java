package com.is.mindart.security.controller;

import lombok.Data;

@Data
public class TerapeutaLoginRequest {
    /**
     * L'email del terapeuta.
     */
    private String email;
    /**
     * La password del terapeuta.
     */
    private String password;
}

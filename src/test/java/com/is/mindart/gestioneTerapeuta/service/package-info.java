/**
 * Questo package contiene le classi di test
 * per il servizio di gestione dei terapeuti
 * all'interno del progetto MindArt.
 * In particolare, include i test unitari per le
 * funzionalità di login dei terapeuti, implementate nel service
 * {@link com.is.mindart.gestioneTerapeuta.service.TerapeutaService}.
 * <p>
 * La classe inclusa in questo package:
 * </p>
 * <ul>
 *     <li>
 * {@link com.is.mindart.gestioneTerapeuta.service.LoginTerapeutaServiceTest}:
 *     Classe di test che verifica il corretto
 *     funzionamento del metodo di login per il terapeuta,
 *     includendo i seguenti scenari:
 *     <ul>
 *         <li>Login con credenziali valide.</li>
 *         <li>Login con password errata.</li>
 *         <li>Login con email inesistente.</li>
 *     </ul>
 *     </li>
 * </ul>
 * <p>
 *     Il package utilizza JUnit 5 e Mockito per
 *     creare test unitari e simulare dipendenze.
 *     </p>
 *     @author mauriliolarocca
 **/
package com.is.mindart.gestioneTerapeuta.service;

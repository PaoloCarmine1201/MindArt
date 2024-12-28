package com.is.mindart.configuration;

import com.is.mindart.security.jwt.JwtChannelInterceptor;
import com.is.mindart.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * JwtChannelInterceptor
     */
    @Autowired
    private JwtUtil jwtTokenProvider;


    /**
     * Configura il broker dei messaggi
     * @param registry
     */
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * registra il percorso per la connessione websocket
     * @param registry
     */
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    /**
     * Configura il canale in ingresso del client
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(jwtTokenProvider));
    }
}

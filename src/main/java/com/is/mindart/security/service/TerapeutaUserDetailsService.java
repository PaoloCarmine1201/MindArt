
package com.is.mindart.security.service;


import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.model.TerapeutaDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TerapeutaUserDetailsService implements UserDetailsService {

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Terapeuta terapeuta = terapeutaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Terapeuta non trovato con email: " + email));
        return new TerapeutaDetails(terapeuta);
    }
}


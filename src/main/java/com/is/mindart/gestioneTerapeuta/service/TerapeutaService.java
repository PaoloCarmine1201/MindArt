package com.is.mindart.gestioneTerapeuta.service;

import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class TerapeutaService {

    /**
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     *  Provvede a mappare l'entità Terapeuta con TerapeutaDTO.
     */
    private final ModelMapper modelMapper;

    /**
     *  Provvede a criptare la password.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     *  Provvede a generare il token JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Provvede alla registrazione del terapeuta.
     * @param terapeutaDto TerapeutaDto
     */
    public void registerTerapeuta(final TerapeutaDTO terapeutaDto) {

        // Hash the password
        String hashedPassword = passwordEncoder.encode(terapeutaDto
                .getPassword());
        terapeutaDto.setPassword(hashedPassword);

        Terapeuta terapeuta = modelMapper.map(terapeutaDto, Terapeuta.class);
        terapeutaRepository.save(terapeuta);
    }

    public TerapeutaDTOStat getTerapeuta(Long id) {
        Terapeuta terapeuta = terapeutaRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Terapeuta non trovato"));
        return modelMapper.map(terapeuta, TerapeutaDTOStat.class);
    }

    /**
     * Provvede a verificare se il terapeuta esiste.
     * @param email Email del terapeuta
     * @return true se il terapeuta esiste, false altrimenti
     */
    public String loginTerapeuta(final String email, final String rawPassword) {
        Terapeuta terapeuta = terapeutaRepository.findByEmail(email)
                .orElse(null);
        if (terapeuta != null) {
            if (passwordEncoder.matches(rawPassword, terapeuta.getPassword())) {
                return jwtUtil.generateToken(terapeuta.getEmail(), "TERAPEUTA");
            }
        }
        return null;
    }

    /**
     * Aggiorna il profile del terapeuta.
     * @param terapeutaDTO TerapeutaDTO con i nuovi
     *                     dati del terapeuta da aggiornare
     * @return TerapeutaDTO con i nuovi dati del terapeuta aggiornato
     */
    public TerapeutaDTOSimple updateTerapeuta(final TerapeutaDTOSimple terapeutaDTO) {
        Terapeuta terapeuta = terapeutaRepository.findById(terapeutaDTO.getId()).orElseThrow(() ->
                new IllegalArgumentException("Terapeuta non trovato"));
        terapeuta.setNome(terapeutaDTO.getNome());
        terapeuta.setCognome(terapeutaDTO.getCognome());
        terapeuta.setEmail(terapeutaDTO.getEmail());
        terapeutaRepository.save(terapeuta);
        return modelMapper.map(terapeuta, TerapeutaDTOSimple.class);
    }
}

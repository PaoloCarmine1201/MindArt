package com.is.mindart.gestioneTerapeuta.service;


import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

    /**
     * Provvede a recuperare il terapeuta.
     * @param email email del terapeuta
     * @return {@link TerapeutaDTOStat}
     */
    public TerapeutaDTOStat getTerapeuta(final String email) {
        Terapeuta terapeuta = terapeutaRepository
                .findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Terapeuta non trovato"));
        return modelMapper.map(terapeuta, TerapeutaDTOStat.class);
    }

    /**
     * Provvede a verificare se il terapeuta esiste.
     * @param email Email del terapeuta
     * @param rawPassword Password in chiaro
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
     * @param email Email del terapeuta
     * @return TerapeutaDTO con i nuovi dati del terapeuta aggiornato
     */
    public TerapeutaDTOSimple updateTerapeuta(
            final TerapeutaDTOSimple terapeutaDTO,
            final String email
    ) {
        Terapeuta terapeuta = terapeutaRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Terapeuta non trovato")
                );
        terapeuta.setNome(terapeutaDTO.getNome());
        terapeuta.setCognome(terapeutaDTO.getCognome());
        terapeuta.setEmail(terapeutaDTO.getEmail());
        terapeutaRepository.save(terapeuta);
        return modelMapper.map(terapeuta, TerapeutaDTOSimple.class);
    }

    /**
     * Cambia la password del terapeuta.
     * @param email Email del terapeuta
     * @param oldPassword Vecchia password
     * @param newPassword Nuova password
     * @return true se la password è stata cambiata, false altrimenti
     */
    public boolean changePassword(
            final String email,
            final String oldPassword,
            final String newPassword) {
        Terapeuta terapeuta = terapeutaRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Terapeuta non trovato")
                );
        if (passwordEncoder.matches(oldPassword, terapeuta.getPassword())) {
            // Hash the password
            String hashedPassword = passwordEncoder.encode(newPassword);
            terapeuta.setPassword(hashedPassword);
            terapeutaRepository.save(terapeuta);
            return true;
        }
        //else
        return false;
    }

}

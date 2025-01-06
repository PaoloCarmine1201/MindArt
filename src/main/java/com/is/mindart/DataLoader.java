package com.is.mindart;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneBambino.model.Sesso;
import com.is.mindart.gestioneCalendario.model.EventoRespository;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe per popolare il DB all'avvio dell'applicazione.
 * Viene eseguita solo se il profilo "dev" è attivo.
 */
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    /**
     * Costante per l'indice 1.
     */
    private static final int INDEX_1 = 1;

    /**
     * Costante per l'indice 2.
     */
    private static final int INDEX_2 = 2;

    /**
     * Costante per l'indice 3.
     */
    private static final int INDEX_3 = 3;

    /**
     * Costante per l'indice 4.
     */
    private static final int INDEX_4 = 4;

    /**
     * Costante che rappresenta il valore 3.
     */
    private static final int THREE = 3;

    /**
     * Costante che rappresenta il valore 4.
     */
    private static final int FOUR = 4;

    /**
     * Costante che rappresenta il valore 5.
     */
    private static final int FIVE = 5;

    /**
     * Punteggio emotivo di 7.
     */
    private static final int EMOTIVE_SCORE_7 = 7;

    /**
     * Punteggio emotivo di 9.
     */
    private static final int EMOTIVE_SCORE_9 = 9;

    /**
     * Punteggio emotivo di 10.
     */
    private static final int EMOTIVE_SCORE_10 = 10;

    /**
     * Repository per la gestione dei dati relativi ai bambini.
     */
    @Autowired
    private BambinoRepository bambinoRepository;

    /**
     * Repository per la gestione degli eventi del calendario.
     */
    @Autowired
    private EventoRespository eventRepository;

    /**
     * Repository per la gestione delle sessioni.
     */
    @Autowired
    private SessioneRepository sessioneRepository;

    /**
     * Repository per la gestione dei materiali.
     */
    @Autowired
    private MaterialeRepository materialeRepository;

    /**
     * Repository per la gestione dei terapeuti.
     */
    @Autowired
    private TerapeutaRepository terapeutaRepository;

    /**
     * Repository per la gestione dei disegni.
     */
    @Autowired
    private DisegnoRepository disegnoRepository;

    /**
     * Template per l'esecuzione di query SQL.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Password encoder per la codifica delle password.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Oggetto per la gestione delle date (formato: "yyyy-MM-dd").
     */
    private final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Oggetto per la gestione delle date e ore
     * (formato: "yyyy-MM-dd HH:mm").
     */
    private final SimpleDateFormat sdfh =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Metodo principale (entry-point) per l’inserimento
     * dei dati di prova nel database.
     *
     * @param args argomenti da linea di comando
     * @throws Exception eccezione generica
     */
    @Override
    public void run(final String... args) throws Exception {
        System.out.println("DataLoader.run");
        try {
            clearDatabase();
            insertTerapeuti();
            insertBambini();
            insertEventi();
            insertMateriali();
            insertSessioni();
            insertDisegni();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Svuota le tabelle del database impostando FOREIGN_KEY_CHECKS a 0,
     * facendo il TRUNCATE e reimpostandolo a 1.
     */
    private void clearDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.execute("TRUNCATE TABLE terapeuta");
        jdbcTemplate.execute("TRUNCATE TABLE bambino");
        jdbcTemplate.execute("TRUNCATE TABLE sessione");
        jdbcTemplate.execute("TRUNCATE TABLE materiale");
        jdbcTemplate.execute("TRUNCATE TABLE disegno");
        jdbcTemplate.execute("TRUNCATE TABLE evento");

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    /**
     * Inserisce i Terapeuti di prova nel database, se non esistono già.
     *
     * @throws Exception eccezione generica
     */
    private void insertTerapeuti() throws Exception {
        if (terapeutaRepository.count() == 0) {
            terapeutaRepository.saveAll(List.of(
                    new Terapeuta(
                            null,
                            "Mario",
                            "Rossi",
                            "mariorossi@gmail.com",
                            sdf.parse("1980-01-01"),
                            passwordEncoder.encode("password123"),
                            null,
                            null,
                            null,
                            null,
                            null
                    ),
                    new Terapeuta(
                            null,
                            "Luca",
                            "Verdi",
                            "lucaverdi@gmail.com",
                            sdf.parse("1985-01-01"),
                            passwordEncoder.encode("password123"),
                            null,
                            null,
                            null,
                            null,
                            null
                    ),
                    new Terapeuta(
                            null,
                            "Giuseppe",
                            "Bianchi",
                            "giuseppebianchi@gmail.com",
                            sdf.parse("1990-01-01"),
                            passwordEncoder.encode("password123"),
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            ));
        }
    }

    /* ------------------------------------------------------------
       Inserimento Bambini - suddiviso in tre metodi più piccoli
       per ridurre la lunghezza complessiva.
       ------------------------------------------------------------ */

    /**
     * Inserisce i Bambini associati al Terapeuta 1.
     *
     * @param terapeuta il terapeuta di riferimento
     * @throws Exception eccezione generica
     */
    private void insertBambiniTerapeuta1(final Terapeuta terapeuta)
            throws Exception {

        bambinoRepository.saveAll(List.of(
                new Bambino(
                        null,
                        "ABC123",
                        "Giovanni",
                        "Rossi",
                        Sesso.MASCHIO,
                        sdf.parse("2015-05-10"),
                        "RSSGNN15A01H501A",
                        "giovanni@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "DEF456",
                        "Luca",
                        "Bianchi",
                        Sesso.MASCHIO,
                        sdf.parse("2016-11-15"),
                        "BNCLCU16B01H501B",
                        "luca@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "GHI789",
                        "Martina",
                        "Verdi",
                        Sesso.FEMMINA,
                        sdf.parse("2017-01-20"),
                        "VRDMRT17C01H501C",
                        "martina@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "JKL012",
                        "Alessandro",
                        "Ferrari",
                        Sesso.MASCHIO,
                        sdf.parse("2014-08-25"),
                        "FRRALS14D01H501D",
                        "alessandro@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "MNO345",
                        "Sofia",
                        "Gialli",
                        Sesso.FEMMINA,
                        sdf.parse("2018-03-10"),
                        "GLISFO18E01H501E",
                        "sofia@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                )
        ));
    }

    /**
     * Inserisce i Bambini associati al Terapeuta 2.
     *
     * @param terapeuta il terapeuta di riferimento
     * @throws Exception eccezione generica
     */
    private void insertBambiniTerapeuta2(final Terapeuta terapeuta)
            throws Exception {

        bambinoRepository.saveAll(List.of(
                new Bambino(
                        null,
                        "PQR678",
                        "Giulia",
                        "Lombardi",
                        Sesso.FEMMINA,
                        sdf.parse("2016-02-17"),
                        "LMBGLI16F01H501F",
                        "giulia@example.com",
                        "+39 667 788 9900",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "STU901",
                        "Marco",
                        "Rossi",
                        Sesso.MASCHIO,
                        sdf.parse("2015-04-22"),
                        "RSSMRK15G01H501G",
                        "marco@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "VWX234",
                        "Francesca",
                        "Negri",
                        Sesso.FEMMINA,
                        sdf.parse("2017-06-12"),
                        "NGRFNC17H01H501H",
                        "francesca@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "YZA567",
                        "Tommaso",
                        "Romano",
                        Sesso.MASCHIO,
                        sdf.parse("2014-09-30"),
                        "RMNTMS14I01H501I",
                        "tommaso@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "BCD890",
                        "Elena",
                        "Bianchi",
                        Sesso.FEMMINA,
                        sdf.parse("2018-11-18"),
                        "BNCELN18J01H501J",
                        "elena@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                )
        ));
    }

    /**
     * Inserisce i Bambini associati al Terapeuta 3.
     *
     * @param terapeuta il terapeuta di riferimento
     * @throws Exception eccezione generica
     */
    private void insertBambiniTerapeuta3(final Terapeuta terapeuta)
            throws Exception {

        bambinoRepository.saveAll(List.of(
                new Bambino(
                        null,
                        "EFG123",
                        "Carlo",
                        "De Luca",
                        Sesso.MASCHIO,
                        sdf.parse("2015-05-05"),
                        "DLCCRL15K01H501K",
                        "carlo@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "HIJ456",
                        "Alice",
                        "Marino",
                        Sesso.FEMMINA,
                        sdf.parse("2016-12-07"),
                        "MRNLIC16L01H501L",
                        "alice@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "KLM789",
                        "Federico",
                        "Corti",
                        Sesso.MASCHIO,
                        sdf.parse("2017-08-17"),
                        "CRTFRD17M01H501M",
                        "federico@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                ),
                new Bambino(
                        null,
                        "QRS345",
                        "Riccardo",
                        "Ferrari",
                        Sesso.MASCHIO,
                        sdf.parse("2018-09-12"),
                        "FRRRCD18O01H501O",
                        "riccardo@example.com",
                        "+39 333 445 6677",
                        terapeuta,
                        null,
                        true
                )
        ));
    }

    /**
     * Inserisce i Bambini di prova nel database,
     * assegnandoli ai vari Terapeuti.
     *
     * @throws Exception eccezione generica
     */
    private void insertBambini() throws Exception {
        List<Terapeuta> terapeuti = terapeutaRepository.findAll();
        if (terapeuti.size() < THREE) {
            return;
        }
        Terapeuta terapeuta1 = terapeuti.getFirst();
        Terapeuta terapeuta2 = terapeuti.get(INDEX_1);
        Terapeuta terapeuta3 = terapeuti.get(INDEX_2);

        insertBambiniTerapeuta1(terapeuta1);
        insertBambiniTerapeuta2(terapeuta2);
        insertBambiniTerapeuta3(terapeuta3);
    }

    /**
     * Inserisce Eventi di prova nel database,
     * associandoli ai vari Terapeuti.
     *
     * @throws Exception eccezione generica
     */
    private void insertEventi() throws Exception {
        List<Terapeuta> terapeuti = terapeutaRepository.findAll();
        if (terapeuti.size() < THREE) {
            return;
        }
        Terapeuta terapeuta1 = terapeuti.getFirst();
        Terapeuta terapeuta2 = terapeuti.get(INDEX_1);
        Terapeuta terapeuta3 = terapeuti.get(INDEX_2);

        eventRepository.saveAll(List.of(
                // Eventi terapeuta1
                new Evento(
                        null,
                        "Visita di controllo",
                        sdfh.parse("2024-12-20 09:00"),
                        sdfh.parse("2024-12-20 10:00"),
                        terapeuta1
                ),
                new Evento(
                        null,
                        "Seduta terapia",
                        sdfh.parse("2024-12-21 14:00"),
                        sdfh.parse("2024-12-21 15:00"),
                        terapeuta1
                ),
                new Evento(
                        null,
                        "Colloquio con i genitori",
                        sdfh.parse("2024-12-22 11:00"),
                        sdfh.parse("2024-12-22 12:00"),
                        terapeuta1
                ),

                // Eventi terapeuta2
                new Evento(
                        null,
                        "Visita di controllo",
                        sdfh.parse("2024-01-18 10:00"),
                        sdfh.parse("2024-01-18 11:00"),
                        terapeuta2
                ),
                new Evento(
                        null,
                        "Seduta terapia",
                        sdfh.parse("2024-01-19 16:00"),
                        sdfh.parse("2024-01-19 17:00"),
                        terapeuta2
                ),
                new Evento(
                        null,
                        "Colloquio con i genitori",
                        sdfh.parse("2024-01-20 09:00"),
                        sdfh.parse("2024-01-20 10:00"),
                        terapeuta2
                ),

                // Eventi terapeuta3
                new Evento(
                        null,
                        "Visita di controllo",
                        sdfh.parse("2024-01-21 09:30"),
                        sdfh.parse("2024-01-21 10:30"),
                        terapeuta3
                ),
                new Evento(
                        null,
                        "Seduta terapia",
                        sdfh.parse("2024-01-22 14:30"),
                        sdfh.parse("2024-01-22 15:30"),
                        terapeuta3
                ),
                new Evento(
                        null,
                        "Colloquio con i genitori",
                        sdfh.parse("2024-01-23 11:00"),
                        sdfh.parse("2024-01-23 12:00"),
                        terapeuta3
                )
        ));
    }

    /**
     * Inserisce Materiali di prova nel database,
     * associandoli ai vari Terapeuti.
     */
    private void insertMateriali() {
        if (materialeRepository.count() == 0) {
            List<Terapeuta> terapeuti = terapeutaRepository.findAll();
            if (terapeuti.size() < THREE) {
                return;
            }
            Terapeuta terapeuta1 = terapeuti.getFirst();
            Terapeuta terapeuta2 = terapeuti.get(INDEX_1);
            Terapeuta terapeuta3 = terapeuti.get(INDEX_2);

            materialeRepository.saveAll(List.of(
                    // Terapeuta1
                    new Materiale(
                            null,
                            "Materiale apprendimento 1",
                            TipoMateriale.IMMAGINE,
                            "/path/to/materiale2",
                            terapeuta1,
                            null
                    ),
                    new Materiale(
                            null,
                            "Materiale colore 1",
                            TipoMateriale.VIDEO,
                            "/path/to/materiale3",
                            terapeuta1,
                            null
                    ),

                    // Terapeuta2
                    new Materiale(
                            null,
                            "Materiale disegno 2",
                            TipoMateriale.PDF,
                            "/path/to/materiale4",
                            terapeuta2,
                            null
                    ),
                    new Materiale(
                            null,
                            "Materiale apprendimento 2",
                            TipoMateriale.IMMAGINE,
                            "/path/to/materiale5",
                            terapeuta2,
                            null
                    ),
                    new Materiale(
                            null,
                            "Materiale colore 2",
                            TipoMateriale.VIDEO,
                            "/path/to/materiale6",
                            terapeuta2,
                            null
                    ),

                    // Terapeuta3
                    new Materiale(
                            null,
                            "Materiale disegno 3",
                            TipoMateriale.PDF,
                            "/path/to/materiale7",
                            terapeuta3,
                            null
                    ),
                    new Materiale(
                            null,
                            "Materiale apprendimento 3",
                            TipoMateriale.IMMAGINE,
                            "/path/to/materiale8",
                            terapeuta3,
                            null
                    ),
                    new Materiale(
                            null,
                            "Materiale colore 3",
                            TipoMateriale.VIDEO,
                            "/path/to/materiale9",
                            terapeuta3,
                            null
                    )
            ));
        }
    }

    /**
     * Inserisce Sessioni di prova nel database,
     * associandole ai vari Bambini e Terapeuti.
     */
    private void insertSessioni() {
        List<Materiale> materiali = materialeRepository.findAll();
        List<Terapeuta> terapeuti = terapeutaRepository.findAll();

        if (materiali.size() < THREE || terapeuti.size() < THREE) {
            return;
        }

        Materiale materiale1 = materiali.getFirst();
        Materiale materiale2 = materiali.get(INDEX_1);
        Materiale materiale3 = materiali.get(INDEX_2);

        Terapeuta terapeuta1 = terapeuti.getFirst();
        Terapeuta terapeuta2 = terapeuti.get(INDEX_1);
        Terapeuta terapeuta3 = terapeuti.get(INDEX_2);

        List<Bambino> bambiniT1 = bambinoRepository
                .findAllByTerapeutaId(terapeuta1.getId());
        List<Bambino> bambiniT2 = bambinoRepository
                .findAllByTerapeutaId(terapeuta2.getId());
        List<Bambino> bambiniT3 = bambinoRepository
                .findAllByTerapeutaId(terapeuta3.getId());

        if (bambiniT1.size() < THREE
                || bambiniT2.size() < THREE
                || bambiniT3.size() < THREE) {
            return;
        }

        Bambino bambino1T1 = bambiniT1.getFirst();
        Bambino bambino2T1 = bambiniT1.get(INDEX_1);
        Bambino bambino3T1 = bambiniT1.get(INDEX_2);

        Bambino bambino1T2 = bambiniT2.getFirst();
        Bambino bambino2T2 = bambiniT2.get(INDEX_1);
        Bambino bambino3T2 = bambiniT2.get(INDEX_2);

        Bambino bambino1T3 = bambiniT3.getFirst();
        Bambino bambino2T3 = bambiniT3.get(INDEX_1);
        Bambino bambino3T3 = bambiniT3.get(INDEX_2);

        sessioneRepository.saveAll(List.of(
                // Sessioni terapeuta1
                new Sessione(
                        null,
                        "Tema di disegno",
                        LocalDateTime.now(),
                        true,
                        "Nota aggiuntiva sulla sessione",
                        TipoSessione.DISEGNO,
                        materiale1,
                        terapeuta1,
                        List.of(bambino1T1)
                ),
                new Sessione(
                        null,
                        "Apprendimento motorio",
                        LocalDateTime.now().plusHours(twoHours()),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.APPRENDIMENTO,
                        materiale2,
                        terapeuta1,
                        List.of(bambino2T1, bambino3T1)
                ),
                new Sessione(
                        null,
                        "Tecniche di colore",
                        LocalDateTime.now().plusDays(1),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.COLORE,
                        materiale3,
                        terapeuta1,
                        List.of(bambino1T1, bambino2T1, bambino3T1)
                ),

                // Sessioni terapeuta2
                new Sessione(
                        null,
                        "Tema di disegno",
                        LocalDateTime.now().plusDays(1),
                        true,
                        "Nota aggiuntiva sulla sessione",
                        TipoSessione.DISEGNO,
                        materiale1,
                        terapeuta2,
                        List.of(bambino1T2, bambino2T2)
                ),
                new Sessione(
                        null,
                        "Apprendimento motorio",
                        LocalDateTime.now().plusDays(1).plusHours(THREE),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.COLORE,
                        materiale2,
                        terapeuta2,
                        List.of(bambino3T2)
                ),
                new Sessione(
                        null,
                        "Tecniche di colore",
                        LocalDateTime.now().plusDays(twoDays()),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.COLORE,
                        materiale3,
                        terapeuta2,
                        List.of(bambino1T2, bambino3T2)
                ),

                // Sessioni terapeuta3
                new Sessione(
                        null,
                        "Tema di disegno",
                        LocalDateTime.now().plusDays(THREE),
                        true,
                        "Nota aggiuntiva sulla sessione",
                        TipoSessione.DISEGNO,
                        materiale1,
                        terapeuta3,
                        List.of(bambino1T3, bambino2T3)
                ),
                new Sessione(
                        null,
                        "Apprendimento motorio",
                        LocalDateTime.now().plusDays(FOUR),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.APPRENDIMENTO,
                        materiale2,
                        terapeuta3,
                        List.of(bambino3T3)
                ),
                new Sessione(
                        null,
                        "Tecniche di colore",
                        LocalDateTime.now().plusDays(FIVE),
                        true,
                        "Osservazioni sul progresso",
                        TipoSessione.COLORE,
                        materiale3,
                        terapeuta3,
                        List.of(bambino2T3)
                )
        ));
    }

    /**
     * Restituisce il valore 2 (ore).
     *
     * @return 2
     */
    private int twoHours() {
        return 2;
    }

    /**
     * Restituisce il valore 2 (giorni).
     *
     * @return 2
     */
    private int twoDays() {
        return 2;
    }

    /**
     * Inserisce Disegni di prova nel database,
     * associandoli alle relative Sessioni e Bambini.
     */
    private void insertDisegni() {
        List<Sessione> sessioni = sessioneRepository.findAll();
        if (sessioni.size() < FIVE) {
            return;
        }

        Sessione sessione1 = sessioni.getFirst();
        Sessione sessione2 = sessioni.get(INDEX_1);
        Sessione sessione3 = sessioni.get(INDEX_2);
        Sessione sessione4 = sessioni.get(INDEX_3);
        Sessione sessione5 = sessioni.get(INDEX_4);

        disegnoRepository.saveAll(List.of(
                new Disegno(
                        null,
                        null,
                        EMOTIVE_SCORE_7,
                        null,
                        sessione1.getData(),
                        ValutazioneEmotiva.FELICE,
                        sessione1.getTerapeuta(),
                        sessione1,
                        sessione1.getBambini()
                ),
                new Disegno(
                        null,
                        null,
                        EMOTIVE_SCORE_9,
                        null,
                        sessione2.getData(),
                        ValutazioneEmotiva.TRISTE,
                        sessione2.getTerapeuta(),
                        sessione2,
                        sessione2.getBambini()
                ),
                new Disegno(
                        null,
                        null,
                        EMOTIVE_SCORE_9,
                        null,
                        sessione3.getData(),
                        ValutazioneEmotiva.FELICE,
                        sessione3.getTerapeuta(),
                        sessione3,
                        sessione3.getBambini()
                ),
                new Disegno(
                        null,
                        null,
                        EMOTIVE_SCORE_10,
                        null,
                        sessione4.getData(),
                        ValutazioneEmotiva.SPAVENTATO,
                        sessione4.getTerapeuta(),
                        sessione4,
                        sessione4.getBambini()
                ),
                new Disegno(
                        null,
                        null,
                        EMOTIVE_SCORE_7,
                        null,
                        sessione5.getData(),
                        ValutazioneEmotiva.ARRABBIATO,
                        sessione5.getTerapeuta(),
                        sessione5,
                        sessione5.getBambini()
                )
        ));
    }

    /**
     * Gestione delle eccezioni tramite log.
     *
     * @param e eccezione da gestire
     */
    private void handleException(final Exception e) {
        System.err.println("Exception Message: " + e.getMessage());
        Logger logger = Logger.getLogger(DataLoader.class.getName());
        logger.severe("Exception occurred: " + e.getMessage());
    }

}

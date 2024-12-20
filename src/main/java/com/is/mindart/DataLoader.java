package com.is.mindart;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneBambino.model.Sesso;
import com.is.mindart.gestioneCalendario.model.EventRespository;
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
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @autor Gabriele Ristallo
 * Classe per popolare il db all'avvio dell'app
 *
 * Questa classe viene eseguita solo se il profilo "dev" è attivo,
 * @code @Profile("dev")
 * @implements CommandLineRunner
 */
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    /**
     * Repository per la gestione dei dati relativi ai bambini.
     */
    @Autowired
    public BambinoRepository bambinoRepository;

    /**
     * Repository per la gestione degli eventi del calendario.
     */
    @Autowired
    public EventRespository eventRepository;

    /**
     * Repository per la gestione delle sessioni.
     */
    @Autowired
    public SessioneRepository sessioneRepository;

    /**
     * Repository per la gestione dei materiali.
     */
    @Autowired
    public MaterialeRepository materialeRepository;

    /**
     * Repository per la gestione dei disegni.
     */
    @Autowired
    public DisegnoRepository disegnoRepository;

    /**
     * Repository per la gestione dei terapeuti.
     */
    @Autowired
    public TerapeutaRepository terapeutaRepository;

    /**
     * Template per l'esecuzione di query SQL.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Oggetto per la gestione delle date.
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Metodo eseguito automaticamente all'avvio dell'applicazione Spring Boot.
     * Ha lo scopo di creare diverse istanze di prova delle entità del sistema
     * e inserirle nel database.
     *
     * @param args
     * @throws Exception se si verifica un errore durante l'esecuzione
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataLoader.run");
        try {
            clearDatabase();
            if (terapeutaRepository.count() == 0) {
                terapeutaRepository.saveAll(List.of(
                    new Terapeuta(null, "Mario", "Rossi", "mariorossi@gmail.com",
                            sdf.parse("1980-01-01"), "password123",
                            null, null, null, null, null),
                    new Terapeuta(null, "Luca", "Verdi", "lucaverdi@gmail.com",
                            sdf.parse("1985-01-01"), "password123",
                            null, null, null, null, null),
                    new Terapeuta(null, "Giuseppe", "Bianchi", "giuseppebianchi@gmail.com",
                            sdf.parse("1990-01-01"), "password123",
                            null, null, null, null, null)
                ));
            } else {
                return;
            }

            List<Terapeuta> terapeuti = terapeutaRepository.findAll();
            Terapeuta terapeuta1 = terapeuti.get(0);
            Terapeuta terapeuta2 = terapeuti.get(1);
            Terapeuta terapeuta3 = terapeuti.get(2);

            bambinoRepository.saveAll(List.of(
                new Bambino(null, "ABC123", "Giovanni", "Rossi", Sesso.MASCHIO,
                        sdf.parse("2015-05-10"),
                        "RSSGNN15A01H501A", "giovanni@example.com",
                        "123456789", terapeuta1, null, null),
                new Bambino(null, "DEF456", "Luca", "Bianchi", Sesso.MASCHIO,
                        sdf.parse("2016-11-15"),
                        "BNCLCU16B01H501B", "luca@example.com",
                        "987654321", terapeuta1, null, null),
                new Bambino(null, "GHI789", "Martina", "Verdi", Sesso.FEMMINA,
                        sdf.parse("2017-01-20"),
                        "VRDMRT17C01H501C", "martina@example.com",
                        "1122334455", terapeuta1, null, null),
                new Bambino(null, "JKL012", "Alessandro", "Ferrari", Sesso.MASCHIO,
                        sdf.parse("2014-08-25"),
                        "FRRALS14D01H501D", "alessandro@example.com",
                        "2233445566", terapeuta1, null, null),
                new Bambino(null, "MNO345", "Sofia", "Gialli", Sesso.FEMMINA,
                        sdf.parse("2018-03-10"),
                        "GLISFO18E01H501E", "sofia@example.com",
                        "5566778899", terapeuta1, null, null),

                new Bambino(null, "PQR678", "Giulia", "Lombardi", Sesso.FEMMINA,
                        sdf.parse("2016-02-17"),
                        "LMBGLI16F01H501F", "giulia@example.com",
                        "3344556677", terapeuta2, null, null),
                new Bambino(null, "STU901", "Marco", "Rossi", Sesso.MASCHIO,
                        sdf.parse("2015-04-22"),
                        "RSSMRK15G01H501G", "marco@example.com",
                        "6677889900", terapeuta2, null, null),
                new Bambino(null, "VWX234", "Francesca", "Negri", Sesso.FEMMINA,
                        sdf.parse("2017-06-12"),
                        "NGRFNC17H01H501H", "francesca@example.com",
                        "9988776655", terapeuta2, null, null),
                new Bambino(null, "YZA567", "Tommaso", "Romano", Sesso.MASCHIO,
                        sdf.parse("2014-09-30"),
                        "RMNTMS14I01H501I", "tommaso@example.com",
                        "4433221100", terapeuta2, null, null),
                new Bambino(null, "BCD890", "Elena", "Bianchi", Sesso.FEMMINA,
                        sdf.parse("2018-11-18"),
                        "BNCELN18J01H501J", "elena@example.com",
                        "5566771100", terapeuta2, null, null),

                new Bambino(null, "EFG123", "Carlo", "De Luca", Sesso.MASCHIO,
                        sdf.parse("2015-05-05"),
                        "DLCCRL15K01H501K", "carlo@example.com",
                        "3344552211", terapeuta3, null, null),
                new Bambino(null, "HIJ456", "Alice", "Marino", Sesso.FEMMINA,
                        sdf.parse("2016-12-07"),
                        "MRNLIC16L01H501L", "alice@example.com",
                        "7788994455", terapeuta3, null, null),
                new Bambino(null, "KLM789", "Federico", "Corti", Sesso.MASCHIO,
                        sdf.parse("2017-08-17"),
                        "CRTFRD17M01H501M", "federico@example.com",
                        "1122334455", terapeuta3, null, null),
                new Bambino(null, "NOP012", "Valentina", "Conti", Sesso.FEMMINA,
                        sdf.parse("2015-03-10"),
                        "CNTVLN15N01H501N", "valentina@example.com",
                        "2233445566", terapeuta3, null, null),
                new Bambino(null, "QRS345", "Riccardo", "Ferrari", Sesso.MASCHIO,
                        sdf.parse("2018-09-12"),
                        "FRRRCD18O01H501O", "riccardo@example.com",
                        "6677889900", terapeuta3, null, null)
            ));

            eventRepository.saveAll(List.of(
                new Evento(null, "Visita di controllo", sdfh.parse("2024-01-15 09:00"),
                        sdfh.parse("2024-01-15 10:00"), terapeuta1),
                new Evento(null, "Seduta terapia", sdfh.parse("2024-01-16 14:00"),
                        sdfh.parse("2024-01-16 15:00"), terapeuta1),
                new Evento(null, "Colloquio con i genitori", sdfh.parse("2024-01-17 11:00"),
                        sdfh.parse("2024-01-17 12:00"), terapeuta1),

                new Evento(null, "Visita di controllo", sdfh.parse("2024-01-18 10:00"),
                        sdfh.parse("2024-01-18 11:00"), terapeuta2),
                new Evento(null, "Seduta terapia", sdfh.parse("2024-01-19 16:00"),
                        sdfh.parse("2024-01-19 17:00"), terapeuta2),
                new Evento(null, "Colloquio con i genitori", sdfh.parse("2024-01-20 09:00"),
                        sdfh.parse("2024-01-20 10:00"), terapeuta2),

                new Evento(null, "Visita di controllo", sdfh.parse("2024-01-21 09:30"),
                        sdfh.parse("2024-01-21 10:30"), terapeuta3),
                new Evento(null, "Seduta terapia", sdfh.parse("2024-01-22 14:30"),
                        sdfh.parse("2024-01-22 15:30"), terapeuta3),
                new Evento(null, "Colloquio con i genitori", sdfh.parse("2024-01-23 11:00"),
                        sdfh.parse("2024-01-23 12:00"), terapeuta3)
            ));

            materialeRepository.saveAll(List.of(
                new Materiale(null, "Materiale disegno 1", TipoMateriale.PDF,
                        "/path/to/materiale1", terapeuta1, null),
                new Materiale(null, "Materiale apprendimento 1", TipoMateriale.IMMAGINE,
                        "/path/to/materiale2", terapeuta1, null),
                new Materiale(null, "Materiale colore 1", TipoMateriale.VIDEO,
                        "/path/to/materiale3", terapeuta1, null),

                new Materiale(null, "Materiale disegno 2", TipoMateriale.PDF,
                        "/path/to/materiale4", terapeuta2, null),
                new Materiale(null, "Materiale apprendimento 2", TipoMateriale.IMMAGINE,
                        "/path/to/materiale5", terapeuta2, null),
                new Materiale(null, "Materiale colore 2", TipoMateriale.VIDEO,
                        "/path/to/materiale6", terapeuta2, null),

                new Materiale(null, "Materiale disegno 3", TipoMateriale.PDF,
                        "/path/to/materiale7", terapeuta3, null),
                new Materiale(null, "Materiale apprendimento 3", TipoMateriale.IMMAGINE,
                        "/path/to/materiale8", terapeuta3, null),
                new Materiale(null, "Materiale colore 3", TipoMateriale.VIDEO,
                        "/path/to/materiale9", terapeuta3, null)
            ));

            List<Materiale> materiali = materialeRepository.findAll();
            List<Terapeuta> terapeuti2 = terapeutaRepository.findAll();
            List<Bambino> bambiniT1 = bambinoRepository.findAllByTerapeutaId(terapeuti2.get(0).getId());
            List<Bambino> bambiniT2 = bambinoRepository.findAllByTerapeutaId(terapeuti2.get(1).getId());
            List<Bambino> bambiniT3 = bambinoRepository.findAllByTerapeutaId(terapeuti2.get(2).getId());

            Materiale materiale1 = materiali.get(0);
            Materiale materiale2 = materiali.get(1);
            Materiale materiale3 = materiali.get(2);

            Bambino bambino1T1 = bambiniT1.get(0);
            Bambino bambino2T1 = bambiniT1.get(1);
            Bambino bambino3T1 = bambiniT1.get(2);
            Bambino bambino1T2 = bambiniT2.get(0);
            Bambino bambino2T2 = bambiniT2.get(1);
            Bambino bambino3T2 = bambiniT2.get(2);
            Bambino bambino1T3 = bambiniT3.get(0);
            Bambino bambino2T3 = bambiniT3.get(1);
            Bambino bambino3T3 = bambiniT3.get(2);

            sessioneRepository.saveAll(List.of(
                new Sessione(null, "Tema di disegno", sdf.parse("2024-01-15"),
                        "Nota aggiuntiva sulla sessione", TipoSessione.DISEGNO, materiale1, terapeuta1,
                        List.of(bambino1T1)),
                new Sessione(null, "Apprendimento motorio", sdf.parse("2024-01-16"),
                        "Osservazioni sul progresso", TipoSessione.APPRENDIMENTO, materiale2, terapeuta1,
                        List.of(bambino2T1, bambino3T1)),
                new Sessione(null, "Tecniche di colore", sdf.parse("2024-01-17"),
                        "Osservazioni sul progresso", TipoSessione.COLORE, materiale3, terapeuta1,
                        List.of(bambino1T1, bambino2T1, bambino3T1)),
                new Sessione(null, "Tema di disegno", sdf.parse("2024-01-18"),
                        "Nota aggiuntiva sulla sessione", TipoSessione.DISEGNO, materiale1, terapeuta2,
                        List.of(bambino1T2, bambino2T2)),
                new Sessione(null, "Apprendimento motorio", sdf.parse("2024-01-19"),
                        "Osservazioni sul progresso", TipoSessione.COLORE, materiale2, terapeuta2,
                        List.of(bambino3T2)),
                new Sessione(null, "Tecniche di colore", sdf.parse("2024-01-20"),
                        "Osservazioni sul progresso", TipoSessione.COLORE, materiale3, terapeuta2,
                        List.of(bambino1T2, bambino3T2)),
                new Sessione(null, "Tema di disegno", sdf.parse("2024-01-21"),
                        "Nota aggiuntiva sulla sessione", TipoSessione.DISEGNO, materiale1, terapeuta3,
                        List.of(bambino1T3, bambino2T3)),
                new Sessione(null, "Apprendimento motorio", sdf.parse("2024-01-22"),
                        "Osservazioni sul progresso", TipoSessione.APPRENDIMENTO, materiale2, terapeuta3,
                        List.of(bambino3T3)),
                new Sessione(null, "Tecniche di colore", sdf.parse("2024-01-23"),
                        "Osservazioni sul progresso", TipoSessione.COLORE, materiale3, terapeuta3,
                        List.of(bambino2T3))
            ));

            List<Sessione> sessioni = sessioneRepository.findAll();
            Sessione sessione1 = sessioni.get(0);
            Sessione sessione2 = sessioni.get(1);
            Sessione sessione3 = sessioni.get(2);
            Sessione sessione4 = sessioni.get(3);
            Sessione sessione5 = sessioni.get(4);

            disegnoRepository.saveAll(List.of(
                new Disegno(null, 7, sessione1.getData(), ValutazioneEmotiva.FELICE,
                        sessione1.getTerapeuta(), sessione1,
                        sessione1.getBambini()),
                new Disegno(null, 9, sessione2.getData(), ValutazioneEmotiva.TRISTE,
                        sessione2.getTerapeuta(), sessione2,
                        sessione2.getBambini()),
                new Disegno(null, 9, sessione3.getData(), ValutazioneEmotiva.FELICE,
                        sessione3.getTerapeuta(), sessione3,
                        sessione3.getBambini()),
                new Disegno(null, 10, sessione4.getData(), ValutazioneEmotiva.SPAVENTATO,
                        sessione3.getTerapeuta(), sessione4,
                        sessione4.getBambini()),
                new Disegno(null, 7, sessione5.getData(), ValutazioneEmotiva.ARRABBIATO,
                        sessione5.getTerapeuta(), sessione5,
                        sessione5.getBambini())
            ));
        } catch (Exception e) {
            System.err.println("Exception Message: " + e.getMessage());

            e.printStackTrace();
        }
    }

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

}

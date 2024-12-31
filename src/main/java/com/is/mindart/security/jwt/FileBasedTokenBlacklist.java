package com.is.mindart.security.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class FileBasedTokenBlacklist {

    /**
     * Il nome del file contenente i token blacklisted.
     */
    private static final String BLACKLIST_FILE = "token_blacklist.txt";
    /**
     * I token blacklisted.
     */
    private Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Aggiunge un token alla blacklist.
     * @param token Il token da aggiungere
     */
    public void addToken(final String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Verifica se un token è blacklisted.
     * @param token Il token da verificare
     * @return true se il token è blacklisted, false altrimenti
     */
    public boolean isTokenBlacklisted(final String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Carica i token blacklisted da file.
     */
    @PostConstruct
    private void loadBlacklistFromFile() {
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(BLACKLIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                blacklistedTokens.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file");
        } finally {
            Runtime.getRuntime().addShutdownHook(
                    new Thread(this::appendTokenToFile));

        }
    }

    @PreDestroy
    private void appendTokenToFile() {
        try (BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(BLACKLIST_FILE))) {
            for (String token : blacklistedTokens) {
                writer.write(token);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file");
        }
    }
}


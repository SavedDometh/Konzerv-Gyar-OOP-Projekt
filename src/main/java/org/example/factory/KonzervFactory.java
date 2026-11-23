package org.example.factory;

import org.example.model.HusKonzerv;
import org.example.model.Konzerv;
import org.example.model.KonzervTipus;
import org.example.model.ZoldsegKonzerv;

import java.time.LocalDate;

public class KonzervFactory {
    private static int nextId = 1;

    private KonzervFactory() {
        // Utility/Factory osztály, nem példányosítható
    }

    /**
     * Központi gyártó metódus a Gyartosor és a Strategy számára.
     * A KonzervTipus alapján automatikusan a megfelelő alosztályt hozza létre.
     */
    public static Konzerv createKonzerv(KonzervTipus tipus) {
        if (tipus == null) {
            throw new IllegalArgumentException("A konzerv típusa nem lehet null!");
        }

        LocalDate today = LocalDate.now();

        return switch (tipus) {
            case CSEMEGEKUKORICA -> createZoldsegKonzerv(tipus, 340, "Szuperédes csemegekukorica", today, false);
            case ZOLDBAB -> createZoldsegKonzerv(tipus, 400, "Vágott zöldbab", today, true);
            case ZOLDBORSO -> createZoldsegKonzerv(tipus, 400, "Zsenge zöldborsó", today, false);
            case MARHAPORKOLT -> createHusKonzerv(tipus, 500, "Marhahús", today);
            case MAJKREM -> createHusKonzerv(tipus, 120, "Sertésmáj", today);
            case TONHAL -> createHusKonzerv(tipus, 300, "Sertéshús", today);
        };
    }

    /**
     * Egyedi paraméterezésű zöldségkonzerv gyártása automatikus ID-val.
     */
    public static Konzerv createZoldsegKonzerv(
            KonzervTipus tipus, int nettoGramm, String vegetableType, LocalDate createdAt, boolean bio
    ) {
        if (tipus == null || !tipus.isZoldseg()) {
            throw new IllegalArgumentException("Érvénytelen típus zöldségkonzervhez: " + tipus);
        }
        return new ZoldsegKonzerv(nextId++, tipus, nettoGramm, vegetableType, createdAt, bio);
    }

    /**
     * Egyedi paraméterezésű húskonzerv gyártása automatikus ID-val.
     */
    public static Konzerv createHusKonzerv(
            KonzervTipus tipus, int nettoGramm, String meatType, LocalDate createdAt
    ) {
        if (tipus == null || !tipus.isHus()) {
            throw new IllegalArgumentException("Érvénytelen típus húskonzervhez: " + tipus);
        }
        return new HusKonzerv(nextId++, tipus, nettoGramm, meatType, createdAt);
    }

    /**
     * JSON fájlból történő visszatöltéskor szinkronizálja a számlálót,
     * hogy az újonnan gyártott konzervek ne kapjanak már létező ID-t.
     */
    public static void syncNextId(int existingId) {
        if (existingId >= nextId) {
            nextId = existingId + 1;
        }
    }
}
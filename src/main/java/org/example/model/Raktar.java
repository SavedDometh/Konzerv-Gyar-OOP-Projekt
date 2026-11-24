package org.example.model;

import org.example.exception.KonzervGyarException;
import org.example.exception.RaktarMegteltException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Raktar {

    //Még nem ellenőriztem hogy jó-e
    //Under development cucc


    private final int maxKapacitas;
    private final List<Konzerv> keszlet;

    public Raktar(int maxKapacitas) {
        if (maxKapacitas <= 0) {
            throw new IllegalArgumentException("A raktár maximális kapacitásának pozitívnak kell lennie!");
        }
        this.maxKapacitas = maxKapacitas;
        this.keszlet = new ArrayList<>();
    }

    /**
     * Új konzerv betárolása a raktárba. Ha a raktár megtelt, saját kivételt dob.
     */
    public void bevetelez(Konzerv konzerv) throws RaktarMegteltException {
        if (konzerv == null) {
            throw new IllegalArgumentException("Nem lehet null konzervet bevételezni!");
        }
        if (isTele()) {
            throw new RaktarMegteltException(
                    "A raktár megtelt! Maximális kapacitás: " + maxKapacitas + " db."
            );
        }
        keszlet.add(konzerv);
    }

    /**
     * Kiszállít (eltávolít) adott mennyiségű konzervet a megadott típusból.
     * Mindig a leghamarabb lejáró tételeket adja ki először (FEFO elv).
     */
    public List<Konzerv> kiszallit(KonzervTipus tipus, int darabszam) throws KonzervGyarException {
        if (tipus == null) {
            throw new IllegalArgumentException("A konzerv típusa nem lehet null!");
        }
        if (darabszam <= 0) {
            throw new IllegalArgumentException("A kiszállítandó darabszámnak pozitívnak kell lennie!");
        }

        int elerheto = getDarabszamTipusSzerint(tipus);
        if (elerheto < darabszam) {
            throw new KonzervGyarException(
                    String.format("Nincs elegendő %s készleten! Kért: %d db, elérhető: %d db.",
                            tipus.getMegnevezes(), darabszam, elerheto)
            );
        }

        // Lejárati dátum szerint sorba rendezzük a megfelelő típusú konzerveket
        List<Konzerv> jeloltek = new ArrayList<>();
        for (Konzerv k : keszlet) {
            if (k.getTipus() == tipus) {
                jeloltek.add(k);
            }
        }
        Collections.sort(jeloltek); // Comparable<Konzerv> alapján lejárat szerint növekvő

        List<Konzerv> kiszallitott = new ArrayList<>();
        for (int i = 0; i < darabszam; i++) {
            Konzerv k = jeloltek.get(i);
            keszlet.remove(k);
            kiszallitott.add(k);
        }

        return kiszallitott;
    }

    /**
     * Eltávolítja a lejárt szavatosságú konzerveket a raktárból.
     * @return A selejtezett konzervek darabszáma.
     */
    public int selejtezLejartakat() {
        int eredetiMeret = keszlet.size();
        keszlet.removeIf(Konzerv::isExpired);
        return eredetiMeret - keszlet.size();
    }

    /**
     * Megszámolja, hogy egy adott konzervtípusból hány darab van jelenleg a raktárban.
     */
    public int getDarabszamTipusSzerint(KonzervTipus tipus) {
        if (tipus == null) {
            return 0;
        }
        int db = 0;
        for (Konzerv k : keszlet) {
            if (k.getTipus() == tipus) {
                db++;
            }
        }
        return db;
    }

    /**
     * Meghatározza, melyik konzervtípusból van a legkevesebb készleten.
     * Ezt használja az AutomatikusHianyAlapuStrategia.
     */
    public KonzervTipus getLeghianyosabbTipus() {
        KonzervTipus leghianyosabb = KonzervTipus.values()[0];
        int minDarab = getDarabszamTipusSzerint(leghianyosabb);

        for (KonzervTipus tipus : KonzervTipus.values()) {
            int aktualisDarab = getDarabszamTipusSzerint(tipus);
            if (aktualisDarab < minDarab) {
                minDarab = aktualisDarab;
                leghianyosabb = tipus;
            }
        }
        return leghianyosabb;
    }

    // --- Rendezett lekérdezések (Comparable és Comparator használata) ---

    /**
     * Visszaadja a teljes készletet lejárati dátum szerint rendezve (Comparable<Konzerv>).
     */
    public List<Konzerv> getKeszletLejaratSzerint() {
        List<Konzerv> rendezett = new ArrayList<>(keszlet);
        Collections.sort(rendezett);
        return Collections.unmodifiableList(rendezett);
    }

    /**
     * Visszaadja a teljes készletet nettó tömeg szerint csökkenő sorrendben (Comparator).
     */
    public List<Konzerv> getKeszletTomegSzerint() {
        List<Konzerv> rendezett = new ArrayList<>(keszlet);
        rendezett.sort(Comparator.comparingInt(Konzerv::getNettoGramm).reversed());
        return Collections.unmodifiableList(rendezett);
    }

    // --- Getterek és állapotlekérdezések (Enkapszuláció) ---

    public List<Konzerv> getKeszlet() {
        return Collections.unmodifiableList(keszlet);
    }

    public int getMaxKapacitas() {
        return maxKapacitas;
    }

    public int getAktualisDarabszam() {
        return keszlet.size();
    }

    public int getSzabadHelyek() {
        return maxKapacitas - keszlet.size();
    }

    public boolean isTele() {
        return keszlet.size() >= maxKapacitas;
    }
}
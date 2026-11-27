package org.example.model;

import org.example.exception.KonzervGyarException;
import org.example.exception.RaktarMegteltException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Raktar {

    private final int maxKapacitas;
    private final List<Konzerv> keszlet;

    public Raktar(int maxKapacitas) {
        if (maxKapacitas <= 0) {
            throw new IllegalArgumentException("A raktár maximális kapacitásának pozitívnak kell lennie!");
        }
        this.maxKapacitas = maxKapacitas;
        this.keszlet = new ArrayList<>();
    }


    public void raktarozas(Konzerv konzerv) throws RaktarMegteltException {
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

        List<Konzerv> jeloltek = new ArrayList<>();
        for (Konzerv k : keszlet) {
            if (k.getTipus() == tipus) {
                jeloltek.add(k);
            }
        }
        Collections.sort(jeloltek);

        List<Konzerv> kiszallitott = new ArrayList<>();
        for (int i = 0; i < darabszam; i++) {
            Konzerv k = jeloltek.get(i);
            keszlet.remove(k);
            kiszallitott.add(k);
        }

        return kiszallitott;
    }

    public int selejtezLejartakat() {
        int eredetiMeret = keszlet.size();
        keszlet.removeIf(Konzerv::isExpired); //inkább hagyom a for-ral való megoldást, ha konzerv.isExpired() true: remove konzerv
        return eredetiMeret - keszlet.size();
    }

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

    public List<Konzerv> getKeszletLejaratSzerint() {
        List<Konzerv> rendezett = new ArrayList<>(keszlet);
        Collections.sort(rendezett);
        return Collections.unmodifiableList(rendezett);
    }

    public List<Konzerv> getKeszlet() {return keszlet;}

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
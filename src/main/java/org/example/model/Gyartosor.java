package org.example.model;

import org.example.exception.ErvenytelenGyartasiMuveletException;
import org.example.exception.RaktarMegteltException;
import org.example.factory.KonzervFactory;
import org.example.strategy.GyartasiStrategia;

import java.util.ArrayList;
import java.util.List;

public class Gyartosor {

    private final int id;
    private int sebesseg;
    private boolean aktiv;
    private GyartasiStrategia strategia;

    public Gyartosor(int id, int sebesseg, GyartasiStrategia strategia) {
        if (id <= 0) {
            throw new IllegalArgumentException("A gyártósor azonosítójának pozitívnak kell lennie!");
        }
        if (sebesseg <= 0) {
            throw new IllegalArgumentException("A gyártási sebességnek pozitívnak kell lennie!");
        }
        if (strategia == null) {
            throw new IllegalArgumentException("A gyártási stratégia nem lehet null!");
        }

        this.id = id;
        this.sebesseg = sebesseg;
        this.aktiv = true;
        this.strategia = strategia;
    }

    public List<Konzerv> gyartCiklus(Raktar raktar) throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        if (!aktiv) {
            throw new ErvenytelenGyartasiMuveletException(
                    "A(z) " + id + " gyártósor jelenleg le van állítva!"
            );
        }
        if (raktar == null) {
            throw new IllegalArgumentException("A célraktár nem lehet null!");
        }
        if (raktar.getSzabadHelyek() < sebesseg){
            throw  new RaktarMegteltException("Raktárban nincs elég hely egy teljes ciklus lefutásához!");
        }
        KonzervTipus kivalasztottTipus = strategia.meghatarozKovetkezoGyartas(raktar);
        List<Konzerv> legyartottAdag = new ArrayList<>();

        for (int i = 0; i < sebesseg; i++) {
            Konzerv ujKonzerv = KonzervFactory.createKonzerv(kivalasztottTipus);
            raktar.raktarozas(ujKonzerv);
            legyartottAdag.add(ujKonzerv);
        }

        return legyartottAdag;
    }

    public int getId() {
        return id;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public void setSebesseg(int sebesseg) {
        if (sebesseg <= 0) {
            throw new IllegalArgumentException("A sebességnek pozitívnak kell lennie!");
        }
        if (sebesseg > 20) {
            throw new IllegalArgumentException("A max sebesség 20!");
        }
        this.sebesseg = sebesseg;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public GyartasiStrategia getStrategia() {
        return strategia;
    }

    public void setStrategia(GyartasiStrategia strategia) {
        if (strategia == null) {
            throw new IllegalArgumentException("A stratégia nem lehet null!");
        }
        this.strategia = strategia;
    }

    @Override
    public String toString() {
        return String.format("[%d] | Állapot: %s | Sebesség: %d db/ciklus | Mód: %s",
                id, aktiv ? "Aktív" : "Leállítva", sebesseg, strategia.getStrategiaNev());
    }
}
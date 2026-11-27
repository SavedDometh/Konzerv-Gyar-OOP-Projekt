package org.example.strategy;

import org.example.model.KonzervTipus;
import org.example.model.Raktar;

public class ManualisGyartasStrategia implements GyartasiStrategia {

    private KonzervTipus celTipus;

    public ManualisGyartasStrategia(KonzervTipus celTipus) {
        if (celTipus == null) {
            throw new IllegalArgumentException("A cél konzervtípus nem lehet null!");
        }
        this.celTipus = celTipus;
    }

    public void setCelTipus(KonzervTipus celTipus) {
        if (celTipus == null) {
            throw new IllegalArgumentException("A cél konzervtípus nem lehet null!");
        }
        this.celTipus = celTipus;
    }

    public KonzervTipus getCelTipus() {
        return celTipus;
    }

    @Override
    public KonzervTipus meghatarozKovetkezoGyartas(Raktar raktar) {
        return celTipus;
    }

    @Override
    public String getStrategiaNev() {
        return "Manuális (" + celTipus.getMegnevezes() + ")";
    }
}
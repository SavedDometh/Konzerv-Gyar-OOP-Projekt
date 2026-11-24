package org.example.strategy;

import org.example.model.KonzervTipus;
import org.example.model.Raktar;

public class AutomatikusHianyAlapuStrategia implements GyartasiStrategia {

    @Override
    public KonzervTipus meghatarozKovetkezoGyartas(Raktar raktar) {
        if (raktar == null) {
            throw new IllegalArgumentException("A raktár referencia nem lehet null!");
        }
        return raktar.getLeghianyosabbTipus();
    }

    @Override
    public String getStrategiaNev() {
        return "Automatikus (Hiányalapú)";
    }
}
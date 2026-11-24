package org.example.strategy;

import org.example.model.KonzervTipus;
import org.example.model.Raktar;

public interface GyartasiStrategia {

    KonzervTipus meghatarozKovetkezoGyartas(Raktar raktar);

    String getStrategiaNev();
}
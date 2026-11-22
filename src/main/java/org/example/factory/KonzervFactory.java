package org.example.factory;

import org.example.model.HusKonzerv;
import org.example.model.Konzerv;
import org.example.model.ZoldsegKonzerv;

import java.time.LocalDate;

public class KonzervFactory {
    private static int nextId = 1;

    private KonzervFactory() {throw new UnsupportedOperationException("A KonzervFactory egy utility osztály, nem példányosítható!");}

    public static Konzerv createZoldsegKonzerv(
            String name, int id, int nettoGramm, String vegetableType, int stock, LocalDate createdAt, boolean bio
    ){
        ZoldsegKonzerv zoldsegkonzerv = new ZoldsegKonzerv(name, id, nettoGramm, vegetableType, stock, createdAt, bio);
        nextId++;
        return  zoldsegkonzerv;
    }

    public static Konzerv createHusKonzerv(
            String nev, int id, int nettoGramm, String meatType, int stock, LocalDate createdAt
    ){
        HusKonzerv huskonzerv = new HusKonzerv(nev, id, nettoGramm, meatType, stock, createdAt);
        nextId++;
        return huskonzerv;
    }

}

package org.example.model;

import java.time.LocalDate;

public class ZoldsegKonzerv extends Konzerv{
    private String zoldsegTipus;

    public ZoldsegKonzerv(String nev, int nettoGramm, String zoldsegTipus, int stock, LocalDate createdAt){
        super(nev, nettoGramm, stock, createdAt, createdAt.plusYears(3));
        if (zoldsegTipus == null || nev.trim().isEmpty()) {
            throw new IllegalArgumentException("A zöldség típusa nem lehet üres!");
        }
        this.zoldsegTipus=zoldsegTipus;
    }

    @Override
    public void printInfo(){
        printBaseInfo();
        System.out.println("Zöldség típusa: " + zoldsegTipus);
    }
}

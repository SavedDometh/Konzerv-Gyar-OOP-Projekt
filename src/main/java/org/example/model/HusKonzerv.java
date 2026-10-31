package org.example.model;

import java.time.LocalDate;

public class HusKonzerv extends Konzerv{
    private String hustipus;

    public HusKonzerv(String nev, int nettoGramm, String husTipus, int stock, LocalDate createdAt){
        super(nev, nettoGramm, stock, createdAt, createdAt.plusYears(3));
        if (husTipus == null || nev.trim().isEmpty()) {
            throw new IllegalArgumentException("A hús típusa nem lehet üres!");
        }
        this.hustipus=husTipus;
    }

    @Override
    public void printInfo(){
        printBaseInfo();
        System.out.println("Hús típusa: " + hustipus);
    }
}

package org.example.model;

import java.time.LocalDate;

public class HusKonzerv extends Konzerv{
    private String meatType;
    //gemini szerint huskonzervnél nem számít a tárolási hőmérséklet 0-30°C között

    public HusKonzerv(String name, int id, int nettoGramm, String meatType, int stock, LocalDate createdAt){
        super(name, id, nettoGramm, stock, createdAt, createdAt.plusYears(5));
        if (meatType == null || this.meatType.trim().isEmpty()) {
            throw new IllegalArgumentException("A hús típusa nem lehet üres!");
        }
        this.meatType = meatType;
    }

    @Override
    public void printInfo(){
        printBaseInfo();
        System.out.println("Hús típusa: " + meatType);
    }
}

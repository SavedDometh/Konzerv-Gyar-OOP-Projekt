package org.example.model;

import java.time.LocalDate;

public class ZoldsegKonzerv extends Konzerv{
    private String vegetableType;
    private boolean bio;
    private int storageTemperature;

    public ZoldsegKonzerv(String nev, int id, int nettoGramm, String vegetableType, int stock, LocalDate createdAt, boolean bio){
        super(nev, id, nettoGramm, stock, createdAt, createdAt.plusYears(3));
        if (vegetableType == null || nev.trim().isEmpty()) {
            throw new IllegalArgumentException("A zöldség típusa nem lehet üres!");
        }
        this.vegetableType = vegetableType;
        this.bio=bio;
        this.storageTemperature =18;
    }

    public boolean getbio(){
        return bio;
    }

    public void setBio(boolean bio){
        this.bio=bio;
    }

    public int getStorageTemperature(){
        return storageTemperature;
    }

    public void setStorageTemperature(int storageTemperature){
        this.storageTemperature = storageTemperature;
    }

    @Override
    public void printInfo(){
        printBaseInfo();
        System.out.println("Zöldség típusa: " + vegetableType);
        System.out.println(("Bio zöldségből készült: " + bio));
    }
}

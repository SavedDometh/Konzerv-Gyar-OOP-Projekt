package org.example.model;

import java.time.LocalDate;

public class ZoldsegKonzerv extends Konzerv{
    private String vegetableType;
    private boolean bio;

    public ZoldsegKonzerv(int id, KonzervTipus tipus, int nettoGramm, String vegetableType, LocalDate createdAt, boolean bio){
        super(id, tipus, nettoGramm, createdAt, createdAt != null ? createdAt.plusYears(3) : null);
        if (vegetableType == null || vegetableType.trim().isEmpty()) {
            throw new IllegalArgumentException("A zöldség típusa nem lehet üres!");
        }
        this.vegetableType = vegetableType;
        this.bio=bio;
    }

    public String getVegetableType(){
        return vegetableType;
    }

    @Override
    public int getRecommendedStorageTemp() {
        return 20;
    }

    public boolean isBio(){
        return bio;
    }

    public void setBio(boolean bio){
        this.bio=bio;
    }

    @Override
    public String toString() {
        return super.toString() + " | Zöldség típusa: " + this.vegetableType + " | Tárolási hőm.: " + getRecommendedStorageTemp() + "°C";
    }

}

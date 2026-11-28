package org.example.model;

import java.time.LocalDate;

public class HusKonzerv extends Konzerv{
    private String meatType;

    public HusKonzerv(int id, KonzervTipus tipus, int nettoGramm, String meatType, LocalDate createdAt){
        super(id, tipus, nettoGramm, createdAt, createdAt != null ? createdAt.plusYears(5) : null);
        if (meatType == null || meatType.trim().isEmpty()) {
            throw new IllegalArgumentException("A hús típusa nem lehet üres!");
        }
        this.meatType = meatType;
    }

    public String getMeatType() {
        return meatType;
    }

    @Override
    public int getRecommendedStorageTemp() {
        return 18;
    }

    @Override
    public String toString() {
        return super.toString() + " | Hús típusa: " + this.meatType + " | Tárolási hőm.: " + getRecommendedStorageTemp() + "°C";
    }
}

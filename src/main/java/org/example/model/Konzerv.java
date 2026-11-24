package org.example.model;

import java.time.LocalDate;

public abstract class Konzerv implements Comparable<Konzerv>{

    private final int id;
    private final KonzervTipus tipus;
    private final LocalDate createdAt;
    private final int nettoGramm;
    private LocalDate expiryDate;


    protected Konzerv(int id, KonzervTipus tipus, int nettoGramm, LocalDate createdAt, LocalDate expiryDate){
        if (id <= 0) {
            throw new IllegalArgumentException("Az azonosítónak pozitívnak kell lennie!");
        }
        if (tipus == null) {
            throw new IllegalArgumentException("A konzerv típusa nem lehet null!");
        }
        if (nettoGramm <= 0) {
            throw new IllegalArgumentException("A nettó tömegnek pozitívnak kell lennie!");
        }
        if (createdAt == null || createdAt.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A gyártási dátum nem lehet érvénytelen vagy jövőbeli!");
        }
        if (expiryDate == null || expiryDate.isBefore(createdAt)) {
            throw new IllegalArgumentException("A lejárati dátum nem lehet korábbi a gyártásnál!");
        }
        this.id=id;
        this.tipus=tipus;
        this.nettoGramm=nettoGramm;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
    }

    public abstract int getRecommendedStorageTemp();

    public final int getId() {
        return id;
    }

    public KonzervTipus getTipus(){return tipus;}

    public final int getNettoGramm(){
        return nettoGramm;
    }

    public LocalDate getCreatedAt(){
        return createdAt;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isExpired(LocalDate currentDate) {
        return currentDate.isAfter(expiryDate);
    }

    @Override
    public int compareTo(Konzerv other) {
        return this.expiryDate.compareTo(other.expiryDate);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %dg | Gyártva: %s | Lejár: %s",
                id, tipus, nettoGramm, createdAt, expiryDate);
    }

}

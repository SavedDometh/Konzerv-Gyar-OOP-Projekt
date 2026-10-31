package org.example.model;

import java.time.LocalDate;

public abstract class Konzerv {
    public static final int MAX_QUANTITY=1000;

    private static int nextid=1;
    private final int id;
    private final String nev;
    private final LocalDate createdAt;
    private final int nettoGramm;
    private int stock;
    private LocalDate lejaratiDatum;


    protected Konzerv(String nev, int nettoGramm, int stock, LocalDate createdAt, LocalDate lejaratiDatum){
        if (nev == null || nev.trim().isEmpty()) {
            throw new IllegalArgumentException("A konzerv neve nem lehet üres!");
        }
        if (nettoGramm <= 0) {
            throw new IllegalArgumentException("A nettó tömegnek pozitívnak kell lennie!");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Nem lehet negatív db konzerv!");
        }
        if (createdAt == null || createdAt.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A gyártási dátum nem lehet érvénytelen vagy jövőbeli!");
        }
        this.nev=nev;
        this.id=nextid++;
        this.nettoGramm=nettoGramm;
        this.stock=stock;
        this.createdAt = createdAt;
        this.lejaratiDatum = lejaratiDatum;
    }

    public final int getId() {
        return id;
    }

    public final int getNettoGramm(){
        return nettoGramm;
    }

    public final String getNev() {
        return nev;
    }

    public int getStock() {
        return stock;
    }

    public LocalDate getCreatedAt(){
        return createdAt;
    }

    public LocalDate getLejaratiDatum() {
        return lejaratiDatum;
    }


    public boolean addStock() {
        if (this.stock + 1 > MAX_QUANTITY) return false;
        this.stock++;
        return true;
    }

    public boolean addStock(int quantity) {
        if (quantity <= 0) return false;
        if (this.stock + quantity > MAX_QUANTITY) return false;
        this.stock += quantity;
        return true;
    }

    public boolean removeStock(int quantity) {
        if (quantity <= 0) return false;
        if (quantity > this.stock) return false;
        this.stock -= quantity;
        return true;
    }

    public boolean removeStock() {
        if (1 > this.stock) return false;
        this.stock--;
        return true;
    }

    public abstract void printInfo();

    protected void printBaseInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + nev);
        System.out.println("Netto weight: " + nettoGramm);
        System.out.println("Stock: " + stock);
        System.out.println("Created at: " + createdAt);
        System.out.println("Lejárati dátum: "+lejaratiDatum);
    }



}

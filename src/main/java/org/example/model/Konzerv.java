package org.example.model;

import java.time.LocalDate;

public abstract class Konzerv implements Comparable<Konzerv>{
    public static final int MAX_QUANTITY=1000;

    private final int id;
    private final String name;
    private final LocalDate createdAt;
    private final int nettoGramm;
    private int stock;
    private LocalDate expiryDate;


    protected Konzerv(String name, int id, int nettoGramm, int stock, LocalDate createdAt, LocalDate expiryDate){
        if (name == null || name.trim().isEmpty()) {
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
        this.name= name;
        this.id=id;
        this.nettoGramm=nettoGramm;
        this.stock=stock;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
    }

    public final int getId() {
        return id;
    }

    public final int getNettoGramm(){
        return nettoGramm;
    }

    public final String getName() {
        return name;
    }

    public int getStock() {
        return stock;
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
        System.out.println("Name: " + name);
        System.out.println("Netto weight: " + nettoGramm);
        System.out.println("Stock: " + stock);
        System.out.println("Created at: " + createdAt);
        System.out.println("Lejárati dátum: "+expiryDate);
    }

    @Override
    public int compareTo(Konzerv other) {
        return this.expiryDate.compareTo(other.expiryDate);
    }


}

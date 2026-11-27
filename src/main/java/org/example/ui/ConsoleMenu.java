package org.example.ui;

import org.example.exception.ErvenytelenGyartasiMuveletException;
import org.example.exception.KonzervGyarException;
import org.example.exception.RaktarMegteltException;
import org.example.model.Gyartosor;
import org.example.model.Konzerv;
import org.example.model.KonzervTipus;
import org.example.model.Raktar;
import org.example.persistence.StorageService;
import org.example.service.GyarManager;
import org.example.strategy.AutomatikusHianyAlapuStrategia;
import org.example.strategy.ManualisGyartasStrategia;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private static final String MENTESI_FAJL = "gyar_allapot.json";

    private GyarManager gyarManager;
    private final StorageService storageService;
    private final Scanner scanner;

    public ConsoleMenu(GyarManager gyarManager, StorageService storageService) {
        if (gyarManager == null || storageService == null) {
            throw new IllegalArgumentException("A GyarManager és a StorageService nem lehet null!");
        }
        this.gyarManager = gyarManager;
        this.storageService = storageService;
        this.scanner = new Scanner(System.in);
    }

    public void indit() {
        boolean fut = true;
        System.out.println("=== KONZERVGYÁR TERMELÉS- ÉS RAKTÁRIRÁNYÍTÓ RENDSZER ===");

        while (fut) {
            kiirFomenu();
            int valasztas = beolvasEgeszSzam("Válassz menüpontot: ");

            try {
                switch (valasztas) {
                    case 1 -> megjelenitAllapot();
                    case 2 -> termelesLepteteseMenu();
                    case 3 -> egyGyartosorInditasa();
                    case 4 -> gyartosorBeallitasa();
                    case 5 -> kiszallitasMenu();
                    case 6 -> keszletEsSelejtezesMenu();
                    case 7 -> allapotMentese();
                    case 8 -> allapotBetoltese();
                    case 0 -> {
                        fut = false;
                        System.out.println("Kilépés a rendszerből. Viszlát!");
                    }
                    default -> System.out.println("Érvénytelen menüpont! Próbáld újra.");
                }
            } catch (RaktarMegteltException e) {
                System.out.println("[RAKTÁR FIGYELMEZTETÉS] " + e.getMessage());
            } catch (ErvenytelenGyartasiMuveletException e) {
                System.out.println("[GYÁRTÁSI HIBA] " + e.getMessage());
            } catch (KonzervGyarException | IllegalArgumentException e) {
                System.out.println("[HIBA] " + e.getMessage());
            }
        }
    }

    private void kiirFomenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("1. Gyár és raktár aktuális állapota");
        System.out.println("2. Termelés léptetése (1 ciklus / több ciklus / 1 nap)");
        System.out.println("3. Egy adott gyártósor soron kívüli indítása");
        System.out.println("4. Gyártósor beállítása (stratégia / állapot / sebesség)");
        System.out.println("5. Konzervek kiszállítása a raktárból");
        System.out.println("6. Raktárkészlet listázása és selejtezés");
        System.out.println("7. Gyár állapotának mentése (JSON)");
        System.out.println("8. Gyár állapotának betöltése (JSON)");
        System.out.println("0. Kilépés");
        System.out.println("--------------------------------------------------");
    }

    private void megjelenitAllapot() {
        Raktar raktar = gyarManager.getRaktar();
        int ciklusok = gyarManager.getElteltCiklusok();
        int napok = ciklusok / GyarManager.NAPI_CIKLUSOK;

        System.out.println("\n--- GYÁRI ÁTTEKINTÉS ---");
        System.out.printf("Eltelt idő: %d ciklus (%d teljes munkanap)%n", ciklusok, napok);
        System.out.printf("Raktár telítettség: %d / %d db (Szabad hely: %d db)%n",
                raktar.getAktualisDarabszam(), raktar.getMaxKapacitas(), raktar.getSzabadHelyek());

        System.out.println("\nGyártósorok:");
        for (Gyartosor sor : gyarManager.getGyartosorok()) {
            System.out.println("  " + sor);
        }

        System.out.println("\nKészlet típusonként:");
        for (KonzervTipus tipus : KonzervTipus.values()) {
            System.out.printf("  - %-18s: %d db%n", tipus.getMegnevezes(), raktar.getDarabszamTipusSzerint(tipus));
        }
    }

    private void termelesLepteteseMenu() throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        System.out.println("\n1 - Egyetlen ciklus léptetése");
        System.out.println("2 - Megadott számú ciklus léptetése");
        System.out.println("3 - Teljes munkanap szimulálása (" + GyarManager.NAPI_CIKLUSOK + " ciklus)");
        int mod = beolvasEgeszSzam("Választás: ");

        List<Konzerv> legyartott;
        switch (mod) {
            case 1 -> legyartott = gyarManager.leptetTermelest();
            case 2 -> {
                int db = beolvasEgeszSzam("Hány ciklust lépjen a termelés? ");
                legyartott = gyarManager.leptetTermelest(db);
            }
            case 3 -> legyartott = gyarManager.szimulalTeljesNapot();
            default -> {
                System.out.println("Érvénytelen választás.");
                return;
            }
        }

        System.out.println("Termelés sikeresen lefutott! Legyártott mennyiség: " + legyartott.size() + " db konzerv.");
    }

    private void egyGyartosorInditasa() throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        int id = beolvasEgeszSzam("Add meg az indítandó gyártósor azonosítóját (pl. 1 vagy 2): ");
        List<Konzerv> adag = gyarManager.gyartEgySoron(id);
        System.out.println("A(z) " + id + "-es gyártósor legyártott " + adag.size() + " db konzervet.");
    }

    private void gyartosorBeallitasa() throws ErvenytelenGyartasiMuveletException {
        for (Gyartosor sor : gyarManager.getGyartosorok()) {
            System.out.println(sor);
        }

        int id = beolvasEgeszSzam("Melyik gyártósort módosítod (ID)? ");
        System.out.println("1 - Állapot váltása (Indítás / Leállítás)");
        System.out.println("2 - Stratégia váltása (Manuális / Automatikus hiányalapú)");
        System.out.println("3 - Ciklussebesség módosítása (db/ciklus)");
        int muvelet = beolvasEgeszSzam("Választás: ");

        switch (muvelet) {
            case 1 -> {
                System.out.println("1 - Aktív (Elindít) | 0 - Inaktív (Leállít)");
                int allapot = beolvasEgeszSzam("Új állapot: ");
                if (allapot != 0 && allapot != 1) {
                    System.out.println("Csak 0 vagy 1 adható meg.");
                    return;
                }
                gyarManager.allitGyartosorAllapotot(id, allapot == 1);
                System.out.println("Gyártósor állapota frissítve.");
            }
            case 2 -> {
                System.out.println("1 - Manuális (fix típus gyártása)");
                System.out.println("2 - Automatikus (leghiányosabb típus pótlása)");
                int strat = beolvasEgeszSzam("Új stratégia: ");
                if (strat == 1) {
                    KonzervTipus tipus = valasztKonzervTipust();
                    gyarManager.allitGyartosorStrategiat(id, new ManualisGyartasStrategia(tipus));
                    System.out.println("Manuális stratégia beállítva: " + tipus.getMegnevezes());
                } else if (strat == 2) {
                    gyarManager.allitGyartosorStrategiat(id, new AutomatikusHianyAlapuStrategia());
                    System.out.println("Automatikus hiányalapú stratégia beállítva.");
                } else {
                    System.out.println("Érvénytelen stratégiaválasztás.");
                }
            }
            case 3 -> {
                int ujSebesseg = beolvasEgeszSzam("Új sebesség (db/ciklus): ");
                gyarManager.allitGyartosorSebesseget(id, ujSebesseg);
                System.out.println("Sebesség módosítva: " + ujSebesseg + " db/ciklus.");
            }
            default -> System.out.println("Érvénytelen választás.");
        }
    }

    private void kiszallitasMenu() throws KonzervGyarException {
        KonzervTipus tipus = valasztKonzervTipust();
        int darabszam = beolvasEgeszSzam("Hány darabot szeretnél kiszállítani? ");
        List<Konzerv> kiszallitott = gyarManager.kiszallitRaktarbol(tipus, darabszam);
        System.out.println("Sikeresen kiszállítva: " + kiszallitott.size() + " db " + tipus.getMegnevezes() + ".");
    }

    private void keszletEsSelejtezesMenu() {
        System.out.println("\n1 - Készlet listázása lejárati dátum szerint (Comparable)");
        System.out.println("2 - Lejárt szavatosságú konzervek selejtezése");
        int valasztas = beolvasEgeszSzam("Választás: ");

        switch (valasztas) {
            case 1 -> kiirKonzervListat(gyarManager.getRaktar().getKeszletLejaratSzerint());
            case 2 -> {
                int selejtezett = gyarManager.selejtezLejartakat();
                System.out.println("Selejtezés kész. Eltávolított lejárt konzervek: " + selejtezett + " db.");
            }
            default -> System.out.println("Érvénytelen választás.");
        }
    }

    private void allapotMentese() throws KonzervGyarException {
        storageService.ment(gyarManager, MENTESI_FAJL);
        System.out.println("A gyár állapota sikeresen elmentve ide: " + MENTESI_FAJL);
    }

    private void allapotBetoltese() throws KonzervGyarException {
        this.gyarManager = storageService.betolt(MENTESI_FAJL);
        System.out.println("A gyár állapota sikeresen visszatöltve innen: " + MENTESI_FAJL);
    }

    private KonzervTipus valasztKonzervTipust() {
        KonzervTipus[] tipusok = KonzervTipus.values();
        System.out.println("\nElérhető konzervtípusok:");
        for (int i = 0; i < tipusok.length; i++) {
            System.out.printf("%d - %s%n", i + 1, tipusok[i].getMegnevezes());
        }

        while (true) {
            int valasztas = beolvasEgeszSzam("Válassz típust (1-" + tipusok.length + "): ");
            if (valasztas >= 1 && valasztas <= tipusok.length) {
                return tipusok[valasztas - 1];
            }
            System.out.println("Érvénytelen sorszám, próbáld újra!");
        }
    }

    private void kiirKonzervListat(List<Konzerv> lista) {
        if (lista.isEmpty()) {
            System.out.println("A raktár jelenleg üres.");
            return;
        }
        System.out.println("\nRaktárkészlet (" + lista.size() + " db):");
        for (Konzerv k : lista) {
            System.out.println("  " + k);
        }
    }

    private int beolvasEgeszSzam(String uzenet) {
        while (true) {
            System.out.print(uzenet);
            String sor = scanner.nextLine().trim();
            try {
                return Integer.parseInt(sor);
            } catch (NumberFormatException e) {
                System.out.println("Érvényes egész számot adj meg!");
            }
        }
    }
}
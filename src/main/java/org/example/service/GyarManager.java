package org.example.service;

import org.example.exception.ErvenytelenGyartasiMuveletException;
import org.example.exception.KonzervGyarException;
import org.example.exception.RaktarMegteltException;
import org.example.model.Gyartosor;
import org.example.model.Konzerv;
import org.example.model.KonzervTipus;
import org.example.model.Raktar;
import org.example.strategy.AutomatikusHianyAlapuStrategia;
import org.example.strategy.GyartasiStrategia;
import org.example.strategy.ManualisGyartasStrategia;

import java.util.ArrayList;
import java.util.List;

public class GyarManager {

    public static final int NAPI_CIKLUSOK = 8; // 1 műszakos, 8 órás munkanap (1 ciklus = 1 üzemóra)

    private Raktar raktar;
    private final List<Gyartosor> gyartosorok;
    private int elteltCiklusok;

    public GyarManager(int raktarKapacitas) {
        this.raktar = new Raktar(raktarKapacitas);
        this.gyartosorok = new ArrayList<>();
        this.elteltCiklusok = 0;

        this.gyartosorok.add(new Gyartosor(1, 5, new ManualisGyartasStrategia(KonzervTipus.CSEMEGEKUKORICA)));
        this.gyartosorok.add(new Gyartosor(2, 5, new AutomatikusHianyAlapuStrategia()));
    }

    public List<Konzerv> leptetTermelest() throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        List<Konzerv> osszesLegyartott = new ArrayList<>();

        for (Gyartosor sor : gyartosorok) {
            if (sor.isAktiv()) {
                List<Konzerv> adag = sor.gyartCiklus(raktar);
                osszesLegyartott.addAll(adag);
            }
        }

        elteltCiklusok++;
        return osszesLegyartott;
    }

    public List<Konzerv> leptetTermelest(int ciklusokSzama)
            throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        if (ciklusokSzama <= 0) {
            throw new IllegalArgumentException("A ciklusok számának pozitívnak kell lennie!");
        }

        List<Konzerv> nagyosszesLegyartott = new ArrayList<>();
        for (int i = 0; i < ciklusokSzama; i++) {
            nagyosszesLegyartott.addAll(leptetTermelest());
        }

        return nagyosszesLegyartott;
    }

    public List<Konzerv> szimulalTeljesNapot()
            throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        return leptetTermelest(NAPI_CIKLUSOK);
    }

    public void allitGyartosorStrategiat(int gyartosorId, GyartasiStrategia ujStrategia)
            throws ErvenytelenGyartasiMuveletException {
        Gyartosor sor = keresGyartosort(gyartosorId);
        sor.setStrategia(ujStrategia);
    }

    public void allitGyartosorAllapotot(int gyartosorId, boolean aktiv)
            throws ErvenytelenGyartasiMuveletException {
        Gyartosor sor = keresGyartosort(gyartosorId);
        sor.setAktiv(aktiv);
    }

    public void allitGyartosorSebesseget(int gyartosorId, int ujSebesseg)
            throws ErvenytelenGyartasiMuveletException {
        Gyartosor sor = keresGyartosort(gyartosorId);
        sor.setSebesseg(ujSebesseg);
    }

    public List<Konzerv> kiszallitRaktarbol(KonzervTipus tipus, int darabszam) throws KonzervGyarException {
        return raktar.kiszallit(tipus, darabszam);
    }

    public int selejtezLejartakat() {
        return raktar.selejtezLejartakat();
    }

    private Gyartosor keresGyartosort(int id) throws ErvenytelenGyartasiMuveletException {
        for (Gyartosor sor : gyartosorok) {
            if (sor.getId() == id) {
                return sor;
            }
        }
        throw new ErvenytelenGyartasiMuveletException("Nem található gyártósor ezzel az azonosítóval: " + id);
    }

    public List<Gyartosor> getGyartosorok() {
        return gyartosorok;
    }

    public int getElteltCiklusok() {
        return elteltCiklusok;
    }


    //persistence dolgok


    public GyarManager(Raktar raktar, List<Gyartosor> gyartosorok) {
        this(raktar, gyartosorok, 0);
    }

    public GyarManager(Raktar raktar, List<Gyartosor> gyartosorok, int elteltCiklusok) {
        if (raktar == null) {
            throw new IllegalArgumentException("A raktár nem lehet null!");
        }
        if (gyartosorok == null || gyartosorok.isEmpty()) {
            throw new IllegalArgumentException("Legalább egy gyártósort meg kell adni!");
        }
        if (elteltCiklusok < 0) {
            throw new IllegalArgumentException("Az eltelt ciklusok száma nem lehet negatív!");
        }
        this.raktar = raktar;
        this.gyartosorok = new ArrayList<>(gyartosorok);
        this.elteltCiklusok = elteltCiklusok;
    }

    public Raktar getRaktar() {
        return raktar;
    }

    public void setRaktar(Raktar raktar) {
        if (raktar == null) {
            throw new IllegalArgumentException("A raktár nem lehet null!");
        }
        this.raktar = raktar;
    }

    public List<Konzerv> gyartEgySoron(int gyartosorId)
            throws ErvenytelenGyartasiMuveletException, RaktarMegteltException {
        Gyartosor sor = keresGyartosort(gyartosorId);
        return sor.gyartCiklus(raktar);
    }
}
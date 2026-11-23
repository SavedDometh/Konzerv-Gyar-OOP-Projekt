package org.example.model;

public enum KonzervTipus {
    CSEMEGEKUKORICA("Csemegekukorica", true),
    ZOLDBAB("Zöldbab", true),
    ZOLDBORSO("Zöldborsó", true),
    MARHAPORKOLT("Marhapörkölt", false),
    MAJKREM("Májkrém", false),
    TONHAL("Tonhal", false);

    private final String megnevezes;
    private final boolean zoldseg;

    KonzervTipus(String megnevezes, boolean zoldseg) {
        this.megnevezes = megnevezes;
        this.zoldseg = zoldseg;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public boolean isZoldseg() {
        return zoldseg;
    }

    public boolean isHus() {
        return !zoldseg;
    }

    @Override
    public String toString() {
        return megnevezes;
    }
}
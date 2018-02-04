package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 1/29/2018.
 */

public class Karir {
    public String karirid;
    public String jenisKrr;
    public int tingkat;

    public String getKaririd() {
        return karirid;
    }

    public String getJenisKrr() {
        return jenisKrr;
    }

    public int getTingkat() {
        return tingkat;
    }

    public Karir() {
    }

    public Karir(String karirid, String jenisKrr, int tingkat) {
        this.karirid = karirid;
        this.jenisKrr = jenisKrr;
        this.tingkat = tingkat;
    }
}

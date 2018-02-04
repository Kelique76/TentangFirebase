package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 2/4/2018.
 */

public class Absensi {
    public String nama, statusOl;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatusOl() {
        return statusOl;
    }

    public void setStatusOl(String statusOl) {
        this.statusOl = statusOl;
    }

    public Absensi(String nama, String statusOl) {
        this.nama = nama;
        this.statusOl = statusOl;
    }

    public Absensi() {
    }
}

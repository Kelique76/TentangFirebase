package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 1/28/2018.
 */

public class Ungguh {

    public String alamatKtr, linkFoto, nama, status;

    public Ungguh() {
    }

    public Ungguh(String alamatKtr, String linkFoto, String nama, String status) {
        this.alamatKtr = alamatKtr;
        this.linkFoto = linkFoto;
        this.nama = nama;
        this.status = status;
    }

    public String getAlamatKtr() {
        return alamatKtr;
    }

    public void setAlamatKtr(String alamatKtr) {
        this.alamatKtr = alamatKtr;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public void setLinkFoto(String linkFoto) {
        this.linkFoto = linkFoto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

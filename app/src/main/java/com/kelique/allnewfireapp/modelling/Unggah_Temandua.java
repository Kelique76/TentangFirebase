package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 1/27/2018.
 */

public class Unggah_Temandua {
    public String id;
    public String nama;
    public String status;
    public String alamatKtr;
    public String linkFoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Unggah_Temandua() {
    }

    public Unggah_Temandua(String id, String nama, String status, String alamatKtr, String linkFoto) {
        this.id = id;
        this.nama = nama;
        this.status = status;
        this.alamatKtr = alamatKtr;
        this.linkFoto = linkFoto;
    }
}

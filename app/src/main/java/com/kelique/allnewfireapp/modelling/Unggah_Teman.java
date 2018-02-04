package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 1/27/2018.
 */

public class Unggah_Teman {
    public String id;
    public String nama;
    public String status;
    public String alamatKtr;


    public Unggah_Teman(String id, String nama, String status, String alamatKtr) {
        this.id = id;
        this.nama = nama;
        this.status = status;
        this.alamatKtr = alamatKtr;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }



    public String getStatus() {
        return status;
    }



    public String getAlamatKtr() {
        return alamatKtr;
    }




}

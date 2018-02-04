package com.kelique.allnewfireapp.modelling;

/**
 * Created by kelique on 1/31/2018.
 */

public class Bloggy {
    private String berita, judul, linkFoto, user_name, datePost, user_foto;

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getUser_foto() {
        return user_foto;
    }

    public void setUser_foto(String user_foto) {
        this.user_foto = user_foto;
    }

    public String getBerita() {
        return berita;
    }

    public void setBerita(String berita) {
        this.berita = berita;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getJudul() {
        return judul;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public void setLinkFoto(String linkFoto) {
        this.linkFoto = linkFoto;
    }

    public Bloggy() {
    }

    public Bloggy(String berita, String judul, String linkFoto, String user_name, String datePost, String user_foto) {
        this.berita = berita;
        this.judul = judul;
        this.linkFoto = linkFoto;
        this.user_name = user_name;
        this.user_foto = user_foto;
        this.datePost = datePost;
    }
}

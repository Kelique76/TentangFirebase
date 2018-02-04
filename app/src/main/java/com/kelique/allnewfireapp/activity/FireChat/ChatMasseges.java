package com.kelique.allnewfireapp.activity.FireChat;

/**
 * Created by kelique on 2/3/2018.
 */

public class ChatMasseges {
    private String isiOcehan;
    private String fotoGoogle;
    private String tglOcehan;

    public ChatMasseges(String isiOcehan, String fotoGoogle, String tglOcehan) {
        this.isiOcehan = isiOcehan;
        this.fotoGoogle = fotoGoogle;
        this.tglOcehan = tglOcehan;
    }

    public String getTglOcehan() {
        return tglOcehan;
    }

    public void setTglOcehan(String tglOcehan) {
        this.tglOcehan = tglOcehan;
    }

    public String getIsiOcehan() {
        return isiOcehan;
    }

    public void setIsiOcehan(String isiOcehan) {
        this.isiOcehan = isiOcehan;
    }

    public String getFotoGoogle() {
        return fotoGoogle;
    }

    public void setFotoGoogle(String fotoGoogle) {
        this.fotoGoogle = fotoGoogle;
    }



    public ChatMasseges() {
    }


}

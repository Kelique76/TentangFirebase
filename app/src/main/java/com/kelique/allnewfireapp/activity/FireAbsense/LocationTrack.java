package com.kelique.allnewfireapp.activity.FireAbsense;

/**
 * Created by kelique on 2/4/2018.
 */

public class LocationTrack {
    String nama;
    String uid;
    String lat;
    String lng;

    public LocationTrack() {
    }

    public LocationTrack(String nama, String uid, String lat, String lng) {
        this.nama = nama;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}

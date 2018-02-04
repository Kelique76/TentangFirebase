package com.kelique.allnewfireapp.activity.FireAbsense;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String nama;
    Double lat, lng;
    private DatabaseReference mDrefLocation;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //TODO: ambil data lokasi dari firebase
        mDrefLocation = FirebaseDatabase.getInstance().getReference("Locations");
        mAuth = FirebaseAuth.getInstance();

        //TODO: menerima data putExtra dng getExtra dari kelas Absensi
        if(getIntent() != null){
            nama = getIntent().getStringExtra("nama");
            lat = getIntent().getDoubleExtra("lat", 0);
            lng = getIntent().getDoubleExtra("lng", 0);
        }
        if(!TextUtils.isEmpty(nama)){
            bukaLokasiUserBerdarakan(nama);
        }


    }

    private void bukaLokasiUserBerdarakan(String nama) {
        Query lokasiuser = mDrefLocation.orderByChild("nama").equalTo(nama);

        lokasiuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot setelahDilihat : dataSnapshot.getChildren())
                {
                    LocationTrack lacakLokasi = setelahDilihat.getValue(LocationTrack.class);
                    //Penanda lokasi partner
                    LatLng lokasiKawan = new LatLng(Double.parseDouble(lacakLokasi.getLat())
                            ,Double.parseDouble(lacakLokasi.getLng()));
                    //TODO: buat lokasi dgn koordinat pemakai (pengguna)
                    Location kita = new Location("");
                    kita.setLatitude(lat);
                    kita.setLongitude(lng);

                    //TODO: buat lokasi kawan lain/partner
                    Location kawan = new Location("");
                    kawan.setLatitude(Double.parseDouble(lacakLokasi.getLat()));
                    kawan.setLongitude(Double.parseDouble(lacakLokasi.getLng()));

                    //TODO: Hitung jarak kita dgn partner
                    pisahJarak(kita, kawan);

                    //TODO: memunculkan penanda lokasi kawan
                    mMap.addMarker(new MarkerOptions().position(lokasiKawan)
                            .title(lacakLokasi.getNama())
                            .snippet("Jarak dari Anda: " + new DecimalFormat("#.#").format(pisahJarak(kita, kawan)))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
                }

                //TODO: penanda keberadaan kita (user)
                String Daku = mAuth.getCurrentUser().getDisplayName();
                LatLng kitaSkrg = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                .position(kitaSkrg)
                .title(Daku)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet("Posisi Anda! "));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private double pisahJarak(Location kita, Location kawan) {
        double jarak = kita.getLatitude() - kawan.getLatitude();
        double pisah = Math.sin(deg2rad(kita.getLatitude()))
                    * Math.sin(deg2rad(kawan.getLatitude()))
                    * Math.cos(deg2rad(kita.getLatitude()))
                    * Math.cos(deg2rad(kawan.getLatitude()))
                    * Math.cos(deg2rad(jarak));
        pisah = Math.acos(pisah);
        pisah = rad2deg(pisah);
        pisah = pisah * 60 * 1.1515;
        return pisah;
    }

    private double rad2deg(double rad) {

        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI/180.0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}

package com.kelique.allnewfireapp.activity.FireAbsense;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.modelling.Absensi;

public class AbsenseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private DatabaseReference fireOl, currentUserOl, sparingOL;
    private FirebaseRecyclerAdapter<Absensi, ListAbsenseViewHolder> mFirebaseRecyclerAdapter;
    private RecyclerView daftarOl;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICE_REQUEST = 1234;
    private static int UPDATE_INTERVAL_LAMA = 5000;
    private static int UPDATE_INTERVAL_CEPAT = 2000;
    private static int JARAK_DLM_METER = 100;
    private GoogleApiClient GAC;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private DatabaseReference mDrefLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absense);

        setTitle("Sistem Absensi");

        mAuth = FirebaseAuth.getInstance();
        mDrefLocation = FirebaseDatabase.getInstance().getReference("Locations");
        fireOl = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        sparingOL = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserOl = sparingOL.child(mAuth.getCurrentUser().getUid());

        if(ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    permission.ACCESS_FINE_LOCATION,
                    permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }else{
            if(checkPlaySvc()){
                buildGoogleApiClient();
                createLocationReq();
                displayLocation();
            }
        }

        daftarOl = findViewById(R.id.rvAbsensi);
        daftarOl.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        daftarOl.setLayoutManager(mLayoutManager);

        jalankanAbsensi();

        memuatDataAbsensi();
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(GAC);
        if(mLocation != null){
            String user = mAuth.getCurrentUser().getUid();
            String namaTampilan = mAuth.getCurrentUser().getDisplayName();
            String uidLoc = mAuth.getCurrentUser().getUid();
            String lat = String.valueOf(mLocation.getLatitude());
            String lng = String.valueOf(mLocation.getLongitude());
            //LocationTrack locate = new LocationTrack();

            mDrefLocation.child(user).setValue(new LocationTrack(
                    namaTampilan,
                    uidLoc,
                    lat,
                    lng
            ));
        }else{
            //Toast.makeText(this, "Tidak Bisa menampilkan lokasi", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Kalau Tidak memuat data");
        }
    }

    private void createLocationReq() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_LAMA);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL_CEPAT);
        mLocationRequest.setSmallestDisplacement(JARAK_DLM_METER);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        GAC = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        GAC.connect();
    }

    private boolean checkPlaySvc() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_REQUEST).show();
            }else{
                Toast.makeText(this, "HPmu tidak Support berbagi Lokasi", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void memuatDataAbsensi() {
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Absensi, ListAbsenseViewHolder>(
                Absensi.class,
                R.layout.isi_presensi_layout,
                ListAbsenseViewHolder.class,
                sparingOL
        ) {
            @Override
            protected void populateViewHolder(ListAbsenseViewHolder viewHolder, final Absensi model, int position) {

                //TODO mulai lihat video lagi pada waktu ke 30 menit setelah punya dua emulator

                final String user = model.getNama();

                Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(viewHolder.imgDP);
                viewHolder.namaPegawai.setText(user);
                viewHolder.mView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(AbsenseActivity.this, "Gua D Kilik kili", Toast.LENGTH_SHORT).show();
                        //String currentUser = mAuth.getCurrentUser().getDisplayName();
                        String currentUser = model.getNama();
                        double usetLat = mLocation.getLatitude();
                        double usetLng = mLocation.getLongitude();
                        if(!model.getNama().equals(mAuth.getCurrentUser().getEmail())){
                            Intent maps = new Intent(AbsenseActivity.this, MapsActivity.class);
                            maps.putExtra("nama", currentUser);
                            maps.putExtra("lat", usetLat);
                            maps.putExtra("lng", usetLng);
                            startActivity(maps);
                        }
                    }
                });
//                viewHolder.mItemClikListener = new ItemClikListener() {
//                    @Override
//                    public void onClick(View view, int position) {
//                        Toast.makeText(AbsenseActivity.this, "Gua D Kilik kili", Toast.LENGTH_SHORT).show();
//
//                        String currentUser = mAuth.getCurrentUser().getDisplayName();
//                        double usetLat = mLocation.getLatitude();
//                        double usetLng = mLocation.getLongitude();
//                        if(!user.equals(currentUser)){
//                            Intent maps = new Intent(AbsenseActivity.this, MapsActivity.class);
//                            maps.putExtra("nama", user);
//                            maps.putExtra("lat", usetLat);
//                            maps.putExtra("lng", usetLng);
//                            startActivity(maps);
//                        }
//
//                    }
//                };



            }
        };
        mFirebaseRecyclerAdapter.notifyDataSetChanged();
        daftarOl.setAdapter(mFirebaseRecyclerAdapter);
    }

    private void jalankanAbsensi() {
        fireOl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class)){
                    String iud = mAuth.getCurrentUser().getUid();
                    //String pesertaOl = new Absensi{mAuth.getCurrentUser().getEmail(), "Online"}
                    currentUserOl.onDisconnect().removeValue();
                    sparingOL.child(iud).setValue(new Absensi(mAuth.getCurrentUser().getDisplayName(), "Online"));
                    mFirebaseRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sparingOL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postData: dataSnapshot.getChildren()){
                    Absensi user = postData.getValue(Absensi.class);

                        Log.d("LOG", "" +user.getNama()+ "is" + user.getStatusOl());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_absensi , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemKeluar:
                currentUserOl.removeValue();
                break;
            case R.id.itemGabung:
                String iud = mAuth.getCurrentUser().getUid();

                sparingOL.child(iud).setValue(new Absensi(mAuth.getCurrentUser().getDisplayName(), "Online"));
                break;
            default:


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(GAC, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        GAC.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        displayLocation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(GAC !=null){
            GAC.connect();
        }
    }

    @Override
    protected void onStop() {
        if(GAC != null){
            GAC.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlaySvc();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlaySvc()){
                        buildGoogleApiClient();
                        createLocationReq();
                        displayLocation();
                    }
                }
            }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

package com.kelique.allnewfireapp.activity.FireSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.halaman.MainActivity;
import com.kelique.allnewfireapp.modelling.Daftar_Teman;
import com.kelique.allnewfireapp.modelling.Unggah_Temandua;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirebaseSearch extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.button_urut)
    FloatingActionButton mButtonUrut;
    @BindView(R.id.button_kedepan)
    FloatingActionButton mButtonKedepan;
    @BindView(R.id.action_cari)
    FloatingActionButton mActionCari;
    @BindView(R.id.multiple_actions_down)
    FloatingActionsMenu mMultipleActionsDown;
    private EditText txtSearch;
    private ImageView imgSearch;
    private ListView myRv;
    private List<Unggah_Temandua> unnguhList;
    private DatabaseReference mDrefUtama;
    private DatabaseReference mDrefCari;
    private DatabaseReference mDrefUrut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_search);
        ButterKnife.bind(this);
        setTitle("Firebase Search");

        unnguhList = new ArrayList<>();

        mDrefUtama = FirebaseDatabase.getInstance().getReference("status_teman");
        mDrefCari = FirebaseDatabase.getInstance().getReference("status_teman");
        mDrefUrut = FirebaseDatabase.getInstance().getReference("status_teman");

        txtSearch = findViewById(R.id.editText);
        imgSearch = findViewById(R.id.imageView);
        myRv = findViewById(R.id.data_hasil_RV);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrefUtama.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                unnguhList.clear();

                for (DataSnapshot temanSS : dataSnapshot.getChildren()) {
                    Unggah_Temandua tmm = temanSS.getValue(Unggah_Temandua.class);
                    unnguhList.add(tmm);
                }
                Daftar_Teman adapter = new Daftar_Teman(FirebaseSearch.this, unnguhList);
                myRv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    private void caridiFirebase(String kataPcarian) {

        Query query = mDrefCari.orderByChild("nama").startAt(kataPcarian).endAt(kataPcarian + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                unnguhList.clear();
                for (DataSnapshot temanSS : dataSnapshot.getChildren()) {
                    Unggah_Temandua tmm = temanSS.getValue(Unggah_Temandua.class);
                    unnguhList.add(tmm);
                }
                Daftar_Teman adapter = new Daftar_Teman(FirebaseSearch.this, unnguhList);
                myRv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }
    //TODO: perbaiki lagi ya
    private void urutFirebase() {
        String myUserId = mDrefUrut.getKey();
        Query query = mDrefUrut.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                unnguhList.clear();
                for (DataSnapshot temanSS : dataSnapshot.getChildren()) {
                    Unggah_Temandua tmm = temanSS.getValue(Unggah_Temandua.class);
                    unnguhList.add(tmm);
                }
                Daftar_Teman adapter = new Daftar_Teman(FirebaseSearch.this, unnguhList);
                myRv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }


    @OnClick({R.id.button_urut, R.id.button_kedepan, R.id.action_cari})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_urut:
                mEditText.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);

                mImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Pencarian Dimulai", Toast.LENGTH_SHORT).show();
                        String kataPcarian = txtSearch.getText().toString();
                        caridiFirebase(kataPcarian);
                    }
                });

                break;
            case R.id.button_kedepan:
               urutFirebase();
                break;
            case R.id.action_cari:
                startActivity(new Intent(FirebaseSearch.this, MainActivity.class));
                break;
        }
    }
}

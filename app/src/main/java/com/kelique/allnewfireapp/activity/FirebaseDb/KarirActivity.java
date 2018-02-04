package com.kelique.allnewfireapp.activity.FirebaseDb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.modelling.Karir;
import com.kelique.allnewfireapp.modelling.Karir_List;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KarirActivity extends AppCompatActivity {


    @BindView(R.id.textKarirTeman)
    TextView mTextKarirTeman;
    @BindView(R.id.karirEdit)
    EditText mKarirEdit;
    @BindView(R.id.seekKarir)
    SeekBar mSeekKarir;
    @BindView(R.id.btnKirim)
    Button mBtnKirim;
    @BindView(R.id.papanKarir)
    Button mPapanKarir;
    @BindView(R.id.lvKarir)
    ListView mLvKarir;
    List<Karir> krList;

    public DatabaseReference dRefKrr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karis);
        ButterKnife.bind(this);



        Intent intrima = getIntent();

        String id = intrima.getStringExtra(FirebaseDBActivity.TEMAN_ID);
        String nama = intrima.getStringExtra(FirebaseDBActivity.TEMAN_NM);

        krList = new ArrayList<>();

        mTextKarirTeman.setText(nama);

        dRefKrr = FirebaseDatabase.getInstance().getReference("karir_teman").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dRefKrr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                krList.clear();
                for(DataSnapshot krSn : dataSnapshot.getChildren()) {
                    Karir newKL = krSn.getValue(Karir.class);
                    krList.add(newKL);
                }

                Karir_List klAdapter = new Karir_List(KarirActivity.this, krList);
                mLvKarir.setAdapter(klAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btnKirim)
    public void onViewClicked() {
        simpanKarir();
    }

    private void simpanKarir() {
        String namaKarir = mKarirEdit.getText().toString().trim();
        int lating = mSeekKarir.getProgress();

        if(!TextUtils.isEmpty(namaKarir)){

            String  id = dRefKrr.push().getKey();

            Karir obj = new Karir(id, namaKarir, lating);
            dRefKrr.child(id).setValue(obj);
            mTextKarirTeman.setText("");
            Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show();

        }else{
            mKarirEdit.setError("Isi datanya");
        }
    }
}

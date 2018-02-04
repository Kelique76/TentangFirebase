package com.kelique.allnewfireapp.halaman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.kelique.allnewfireapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarActivity extends AppCompatActivity {


    @BindView(R.id.editTxtMailDftr)
    EditText mEditTxtMailDftr;
    @BindView(R.id.editTxtPassDftr)
    EditText mEditTxtPassDftr;
    @BindView(R.id.editTxtPassUlng)
    EditText mEditTxtPassUlng;
    @BindView(R.id.buttonKirim)
    Button mButtonKirim;

    private FirebaseAuth mAuth;
    private ProgressDialog mProg;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        //mRef = FirebaseDatabase.getInstance().getReference().child("Agen");
        mProg = new ProgressDialog(this);
    }

    @OnClick(R.id.buttonKirim)
    public void onViewClicked() {
        daftarKan();
    }

    private void daftarKan() {


        final String mail = mEditTxtMailDftr.getText().toString();
        String pass = mEditTxtPassDftr.getText().toString();
        String passtwo = mEditTxtPassUlng.getText().toString();


        if(TextUtils.isEmpty(mail)){mEditTxtMailDftr.setError("Harus diisi");}
        else if(TextUtils.isEmpty(pass)){mEditTxtPassDftr.setError("Harus diisi");}
        else if(TextUtils.isEmpty(passtwo) || !passtwo.equals(pass)){mEditTxtPassUlng.setError("Password Berbeda");}
        else{
            mProg.setTitle("Mendaftarkan Pengguna");
            mProg.show();
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //TODO: apabila tidak langsung mau create Profile maka code dibawah tetap di komen
//                        String user_id = mAuth.getCurrentUser().getUid();
//
//                        DatabaseReference databaseAgen = mRef.child(user_id);
//                        databaseAgen.child("nama").setValue(nama);
//                        databaseAgen.child("email_terdaftar").setValue(mail);
//                        databaseAgen.child("nomor_HP").setValue(hpe);
//
//                        databaseAgen.child("foto_profile").setValue("default");
                        mProg.dismiss();
                        Toast.makeText(DaftarActivity.this, "Pendaftaran Selesai", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DaftarActivity.this, LoginActivity.class));
                    }

                }
            });
        }

    }
}

package com.kelique.allnewfireapp.halaman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.activity.FireBlog.SetupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "penanda";
    @BindView(R.id.editText2)
    EditText mEditText2;
    @BindView(R.id.editText3)
    EditText mEditText3;
    @BindView(R.id.button2)
    Button mButton2;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.textView3)
    TextView mTextView3;
    @BindView(R.id.imgFb)
    ImageView mImgFb;
    @BindView(R.id.imgTwit)
    ImageView mImgTwit;
    @BindView(R.id.imgGoogle)
    ImageView mImgGoogle;

    private FirebaseAuth mAuth;
    private DatabaseReference dRefAgen;
    private ProgressDialog mDialog;
    private SignInButton siBtn;
    private static final int RC_SIGN_IN = 21;
    private GoogleApiClient gaClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        dRefAgen = FirebaseDatabase.getInstance().getReference().child("Agen");
        dRefAgen.keepSynced(true);
        mDialog = new ProgressDialog(this);
        mAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "Anda Berhasil Masuk", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(LoginActivity.this, "Gagal Masuk!!!", Toast.LENGTH_SHORT).show();
                }
            }
        };


        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gaClient = new Builder(this)
                .enableAutoManage(this, new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "gagal masuk karena" + connectionResult, Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gsio).build();

            }

    public void signInProsess() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gaClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mDialog.setTitle("Masuk dengan Akun Google Anda...");
            mDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                mDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(this, "Berhasil Masuk Dengan Akun Google", Toast.LENGTH_SHORT).show();
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                mDialog.dismiss();
                Toast.makeText(this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            userNyaPunyaAkunTidak();

                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }


    @OnClick({R.id.button2, R.id.imgFb, R.id.imgTwit, R.id.imgGoogle, R.id.textView2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button2:

                checkInDulu();
                break;
            case R.id.textView2:
                startActivity(new Intent(LoginActivity.this, DaftarActivity.class));
                break;
            case R.id.imgFb:
                break;
            case R.id.imgTwit:
                break;
            case R.id.imgGoogle:
                signInProsess();
                break;
        }
    }

    private void checkInDulu() {

        String emaillgn = mEditText2.getText().toString().trim();
        String passlgn = mEditText3.getText().toString().trim();

        if (TextUtils.isEmpty(emaillgn)) {
            mEditText2.setError("Isi dengan email");
        } else if (TextUtils.isEmpty(passlgn)) {
            mEditText3.setError("Jgn dikosongkan");
        } else {
            mDialog.setTitle("Proses Masuk ...");
            mDialog.show();
            mAuth.signInWithEmailAndPassword(emaillgn, passlgn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userNyaPunyaAkunTidak();

                        mDialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                }
            });
        }

    }

    private void userNyaPunyaAkunTidak() {
        if(mAuth.getCurrentUser() !=null) {
            final String id_user = mAuth.getCurrentUser().getUid();
            //TODO: untuk mengecek user_id yang login ada/telah terdaftar belum di database Agen
            dRefAgen.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(id_user)) {
                        Intent logInten = new Intent(LoginActivity.this, MainActivity.class);
                        logInten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logInten);


                    } else {
                        Intent setupInten = new Intent(LoginActivity.this, SetupActivity.class);
                        setupInten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupInten);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

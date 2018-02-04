package com.kelique.allnewfireapp.activity.FireChat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.halaman.LoginActivity;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FireChatActivity extends AppCompatActivity {

    @BindView(R.id.wadah_chat)
    ListView mWadahChat;
    @BindView(R.id.wadah_kirim_Gbr)
    ImageView mWadahKirimGbr;
    @BindView(R.id.chat_papan)
    TextInputEditText mChatPapan;
    @BindView(R.id.chat_fab)
    FloatingActionButton mChatFab;
    @BindView(R.id.linearLayout2)
    LinearLayout mLinearLayout2;
    FirebaseListAdapter<ChatMasseges> mChatMassegesFirebaseListAdapter;
    FirebaseAuth mAuth;
    DatabaseReference dRefChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_chat);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        dRefChat = FirebaseDatabase.getInstance().getReference().child("emakemak");

        tampilkanObrolan();
    }

    private void tampilkanObrolan() {
        mChatMassegesFirebaseListAdapter = new FirebaseListAdapter<ChatMasseges>(
                this,
                ChatMasseges.class,
                R.layout.list_isi_chat_layout,
                dRefChat
        ) {
            @Override
            protected void populateView(View v, ChatMasseges model, int position) {
                //TODO: memasukkan data ke isi chat layout (sbg list itemnya)

                TextView isiPesanTxt, pengirimPsn, tglKirim;
                ImageView gbrBulet;

                isiPesanTxt = v.findViewById(R.id.txtIsi_obrolan);
                pengirimPsn = v.findViewById(R.id.txt_nama_pengoceh);
                tglKirim = v.findViewById(R.id.txt_tgl_obrolan);
                gbrBulet = v.findViewById(R.id.circleImage);

                isiPesanTxt.setText(model.getIsiOcehan());
                pengirimPsn.setText(model.getFotoGoogle());
                tglKirim.setText(model.getTglOcehan());
                Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(gbrBulet);

            }
        };
        mWadahChat.setAdapter(mChatMassegesFirebaseListAdapter);

    }

    @OnClick({R.id.wadah_kirim_Gbr, R.id.chat_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wadah_kirim_Gbr:
                break;
            case R.id.chat_fab:
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                final String formatTgl = sdf.format(date);
                final String isiObrolan = mChatPapan.getText().toString();
                final DatabaseReference chatData = dRefChat.push();
                final String fotoGtg = mAuth.getCurrentUser().getDisplayName();

                chatData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        chatData.child("isiOcehan").setValue(isiObrolan);
                        chatData.child("fotoGoogle").setValue(fotoGtg);
                        chatData.child("tglOcehan").setValue(formatTgl);

                        mChatPapan.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.keluar:
                mAuth.signOut();
                startActivity(new Intent(FireChatActivity.this, LoginActivity.class));
                finish();
                break;
                default:


        }

        return super.onOptionsItemSelected(item);
    }
}

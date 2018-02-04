package com.kelique.allnewfireapp.halaman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.firebase.auth.FirebaseAuth;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.activity.FireAbsense.AbsenseActivity;
import com.kelique.allnewfireapp.activity.FireBlog.MiniBlokActivity;
import com.kelique.allnewfireapp.activity.FireChat.FireChatActivity;
import com.kelique.allnewfireapp.activity.FireSearch.FirebaseSearch;
import com.kelique.allnewfireapp.activity.FirebaseDb.FirebaseDBActivity;

public class MainActivity extends AppCompatActivity {

    CardView Fdb, cCgm, cd, cv, ci;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Selamat Datang");

        Fdb = findViewById(R.id.cardFirebase);
        cd = findViewById(R.id.cardCari);
        cCgm = findViewById(R.id.cardGyatMasjid);
        cv = findViewById(R.id.collapToolBar);
        ci = findViewById(R.id.fireAbsen);
        mAuth = FirebaseAuth.getInstance();

        ci.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AbsenseActivity.class));
            }
        });
        cv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FireChatActivity.class));
            }
        });

        Fdb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FirebaseDBActivity.class));
            }
        });

        cd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FirebaseSearch.class));
            }
        });

        cCgm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MiniBlokActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}

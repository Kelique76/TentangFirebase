package com.kelique.allnewfireapp.activity.FireBlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.halaman.LoginActivity;
import com.kelique.allnewfireapp.modelling.Bloggy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiniBlokActivity extends AppCompatActivity {

    @BindView(R.id.wadah_utama_blog)
    RecyclerView mWadahUtamaBlog;

    private DatabaseReference mDblog;
    private DatabaseReference dRefAgen;
    private DatabaseReference mDataLike;
    private DatabaseReference mDataCurrentUser;
    private Query mQueryCurrentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean mLikeClik = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_blok);
        ButterKnife.bind(this);
        setTitle("Mini Blog");

        mDblog = FirebaseDatabase.getInstance().getReference().child("blogPost");
        mDataLike = FirebaseDatabase.getInstance().getReference().child("ILikeIt");
        dRefAgen = FirebaseDatabase.getInstance().getReference().child("Agen");
        mAuth = FirebaseAuth.getInstance();

        //TODO: ini digunakan kalau mau shorting (mengurutkan data) sesuai current user (belum buat sharedPreff???)
        String currentUser = mAuth.getCurrentUser().getUid();
        mDataCurrentUser = FirebaseDatabase.getInstance().getReference().child("blogPost");
        mQueryCurrentUser = mDataCurrentUser.orderByChild("userId").equalTo(currentUser);


        mAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //TODO: ini untuk mengecek apakah USer sedang Login atau Tidak
                //TODO: bila kosong/tidak ada aktifitas masuk maka diarahkan ke LoginAktivity lagih
                if(firebaseAuth.getCurrentUser() == null){
                    Intent keDepanlagi = new Intent(MiniBlokActivity.this, LoginActivity.class);
                    keDepanlagi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(keDepanlagi);
                }
            }
        };
        mDblog.keepSynced(true);

        dRefAgen.keepSynced(true);
        mDataLike.keepSynced(true);
        mWadahUtamaBlog.setHasFixedSize(true);
        mWadahUtamaBlog.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        userNyaadaTidak();
        FirebaseRecyclerAdapter<Bloggy, BlogViewHolder> frRA = new FirebaseRecyclerAdapter<Bloggy, BlogViewHolder>(
                Bloggy.class,
                //TODO: pastikan yang dibuat adalah child dari
                R.layout.isi_list_blog_layout,
                BlogViewHolder.class,
                mDblog
                //mQueryCurrentUser

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Bloggy model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setJudul(model.getJudul());
                viewHolder.setBerita(model.getBerita());
                viewHolder.setLinkFoto(model.getLinkFoto());
                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setWaktu(model.getDatePost());
                viewHolder.setImage_user(model.getUser_foto());

                viewHolder.setBtnLike(post_key);

                viewHolder.mView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intenBlog = new Intent(MiniBlokActivity.this, BlogActivity.class);
                        intenBlog.putExtra("boggy", post_key);
                        startActivity(intenBlog);
                    }
                });

                viewHolder.mPic.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mLikeClik = true;

                            mDataLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mLikeClik) {
                                        //TODO: Jika pengguna telah like sebelumnya dan datanya ada
                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                            mDataLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            Toast.makeText(MiniBlokActivity.this, "Inalillahi, Anda tak suka?", Toast.LENGTH_SHORT).show();
                                            mLikeClik = false;
                                        } else {
                                            //TODO: kalau belum then do these
                                            mDataLike.child(post_key).child(mAuth.getCurrentUser().getUid())
                                                    .setValue(mAuth.getCurrentUser().getDisplayName());
                                            Toast.makeText(MiniBlokActivity.this, "Alhamdulillah, Anda Suka!", Toast.LENGTH_SHORT).show();
                                            mLikeClik = false;

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }
                });

            }
        };

        mWadahUtamaBlog.setAdapter(frRA);

    }

    private void userNyaadaTidak() {

        final String id_user = mAuth.getCurrentUser().getUid();
        //TODO: untuk mengecek user_id yang login ada/telah terdaftar belum di database Agen
        dRefAgen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(id_user)){
                    Intent setupInten = new Intent(MiniBlokActivity.this, SetupActivity.class);
                    setupInten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupInten);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView mPic;
        DatabaseReference mDataLikeBV;
        FirebaseAuth mAuthBV;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mPic = mView.findViewById(R.id.ilikePic);
            mDataLikeBV = FirebaseDatabase.getInstance().getReference().child("ILikeIt");
            mDataLikeBV.keepSynced(true);
            mAuthBV = FirebaseAuth.getInstance();
        }

        public void setBtnLike(final String post_key){

            mDataLikeBV.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuthBV.getCurrentUser().getUid())){
                        mPic.setImageResource(R.drawable.ic_thumb_up_kuning);
                    }else{
                        mPic.setImageResource(R.drawable.ic_thumb_up_white_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setJudul(String judul) {
            TextView txtJudul = mView.findViewById(R.id.tvIsiJudul);
            txtJudul.setText(judul);


        }
        public void  setBerita(String berita){
            TextView txtIsiBlog = mView.findViewById(R.id.tvIsiBerita);
            txtIsiBlog.setText(berita);
        }
        public void setLinkFoto(String linkFoto){
            ImageView imgBlg = mView.findViewById(R.id.gbrBlog);
            //Glide.with(Context).load(linkFoto).into(imgBlg);
            Picasso.with(mView.getContext()).load(linkFoto).into(imgBlg);
        }
        public void setUser_name (String user_name){
            TextView txtUsrnm = mView.findViewById(R.id.tvNamaPengarang);
            txtUsrnm.setText(user_name);
        }
        public void setImage_user(String user_foto){
            ImageView imgPew = mView.findViewById(R.id.circleImage);
            Glide.with(mView.getContext()).load(user_foto).into(imgPew);
        }
        public void setWaktu (String datePost){
            TextView txtWkt = mView.findViewById(R.id.isitglPost);
            txtWkt.setText(datePost);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {

        } else if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MiniBlokActivity.this, PostActivity.class));
        }else if (item.getItemId() == R.id.action_urut_id) {
            //startActivity(new Intent(MiniBlokActivity.this, PostActivity.class));
        }else if (item.getItemId() == R.id.action_urut_nama) {
            //startActivity(new Intent(MiniBlokActivity.this, PostActivity.class));
        }else if (item.getItemId() == R.id.action_urut_wakty) {
            //startActivity(new Intent(MiniBlokActivity.this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

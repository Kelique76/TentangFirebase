package com.kelique.allnewfireapp.activity.FireBlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jackandphantom.circularimageview.CircleImage;
import com.kelique.allnewfireapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlogActivity extends AppCompatActivity {
    @BindView(R.id.circleImage)
    CircleImage mCircleImage;
    @BindView(R.id.txtJudul)
    TextView mTxtJudul;
    @BindView(R.id.txtNamaPenulis)
    TextView mTxtNamaPenulis;
    @BindView(R.id.textView4)
    TextView mTextView4;
    @BindView(R.id.txtTgl)
    TextView mTxtTgl;
    @BindView(R.id.txtWadahUtama)
    TextView mTxtWadahUtama;
    @BindView(R.id.wadahGbr)
    ImageView mWadahGbr;
    @BindView(R.id.btnHapus)
    Button mBtnHapus;
    private DatabaseReference dRef;
    private String blog_id = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        ButterKnife.bind(this);


        blog_id = getIntent().getExtras().getString("boggy");
        dRef = FirebaseDatabase.getInstance().getReference().child("blogPost");
        dRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        dRef.child(blog_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String judulBlog = (String) dataSnapshot.child("judul").getValue();

                String tglBlog = (String) dataSnapshot.child("datePost").getValue();
                String britaBlog = (String) dataSnapshot.child("berita").getValue();
                String fotoBlog = (String) dataSnapshot.child("linkFoto").getValue();
                String username = (String) dataSnapshot.child("user_name").getValue();
                String userBlog = (String) dataSnapshot.child("user_foto").getValue();
                Picasso.with(getApplicationContext()).load(fotoBlog).into(mWadahGbr);
                Glide.with(getBaseContext()).load(userBlog).into(mCircleImage);
                mTxtJudul.setText(judulBlog);
                mTxtNamaPenulis.setText(username);
                mTxtTgl.setText(tglBlog);
                mTxtWadahUtama.setText(britaBlog);

                if(mAuth.getCurrentUser().getUid() == blog_id){
                    mBtnHapus.setVisibility(View.VISIBLE);

                }
                mBtnHapus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dRef.child(blog_id).removeValue();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.btnHapus)
    public void onViewClicked() {
    }
}

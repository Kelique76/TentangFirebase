package com.kelique.allnewfireapp.activity.FireBlog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.kelique.allnewfireapp.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.imgButton)
    ImageView mImgButton;
    @BindView(R.id.judulBrita)
    EditText mJudulBrita;
    @BindView(R.id.isiBrita)
    EditText mIsiBrita;
    @BindView(R.id.btnPost)
    Button mBtnPost;

    private static final int GALERRY_REQ = 1;
    private static final int RESULT_CROP = 12;
    private static final int MAX_LENGTH = 5;
    private String fotoPath;
    private Uri imageUri = null;

    private StorageReference mStorageReference;
    private DatabaseReference mDBReference;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDBReference = FirebaseDatabase.getInstance().getReference().child("blogPost");
        mProgressDialog = new ProgressDialog(this);
        mAuth =FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDataUser = FirebaseDatabase.getInstance().getReference().child("Agen").child(mFirebaseUser.getUid());

    }

    @OnClick({R.id.imgButton, R.id.btnPost})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("Foto_Blog/*");
                startActivityForResult(intent, GALERRY_REQ);

                break;
            case R.id.btnPost:
                //Toast.makeText(this, "mau kirim data", Toast.LENGTH_SHORT).show();
                kirimBerita();

                break;
        }
    }

    private void kirimBerita() {
        mProgressDialog.setMessage("Menyimpan Data Blog");

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        final String formatTgl = sdf.format(date);

        final String isiJudul = mJudulBrita.getText().toString().trim();
        final String isiBrita = mIsiBrita.getText().toString().trim();

        if(!TextUtils.isEmpty(isiJudul) && !TextUtils.isEmpty(isiBrita) && imageUri !=null){
            mProgressDialog.show();
            StorageReference mStoreFoto = mStorageReference.child("foto_blog").child(imageUri.getLastPathSegment());
            mStoreFoto.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    final String alamatDl = taskSnapshot.getDownloadUrl().toString();
                    final DatabaseReference newPost = mDBReference.push();


                    mDataUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("judul").setValue(isiJudul);
                            newPost.child("berita").setValue(isiBrita);
                            newPost.child("linkFoto").setValue(alamatDl);
                            newPost.child("datePost").setValue(formatTgl);
                            newPost.child("userId").setValue(mFirebaseUser.getUid());
                            //TODO: yang di ambil dari db child agen adalah nama_agen yang di masukkan ke variable user_name
                            newPost.child("user_name").setValue(dataSnapshot.child("nama_agen").getValue());
                            //TODO: yang di ambil dari db dgn child agen adalah foto_profile yang di masukkan ke variable user_foto
                            newPost.child("user_foto").setValue(dataSnapshot.child("foto_profile").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(PostActivity.this, "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(PostActivity.this, MiniBlokActivity.class));
                                            } else{
                                                Toast.makeText(PostActivity.this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgressDialog.dismiss();


                }
            });
        }


    }

//    public static String random() {
//        Random generator = new Random();
//        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(MAX_LENGTH);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
//        return randomStringBuilder.toString();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALERRY_REQ && resultCode==RESULT_OK){

            imageUri = data.getData();
            mImgButton.setImageURI(imageUri);

        }
    }


}

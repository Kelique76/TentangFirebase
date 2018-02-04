package com.kelique.allnewfireapp.activity.FireBlog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.jackandphantom.circularimageview.CircleImage;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.halaman.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImage.ActivityResult;
import com.theartofdev.edmodo.cropper.CropImageView.Guidelines;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupActivity extends AppCompatActivity {


    @BindView(R.id.editTextNama)
    EditText mEditTextNama;
    @BindView(R.id.editTextHP)
    EditText mEditTextHP;
    @BindView(R.id.editTextAlamt)
    EditText mEditTextAlamt;
    @BindView(R.id.btnSiman)
    Button mBtnSiman;
    Uri myUri;
    @BindView(R.id.circleImage)
    CircleImage mCircleImage;
    @BindView(R.id.imgCamera)
    ImageView mImgCamera;
    private Uri resultUri = null;
    private DatabaseReference dRefAgen;
    private ProgressDialog mProl;
    private FirebaseAuth mAuth;
    private StorageReference stoRef;

    private static final int GALERY_CODEREQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        setTitle("Pembuatan Profile");
        dRefAgen = FirebaseDatabase.getInstance().getReference().child("Agen");
        mProl = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        stoRef = FirebaseStorage.getInstance().getReference().child("ftprofile_agen");
    }

    @OnClick({R.id.imgCamera, R.id.btnSiman})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCamera:
                Intent galnten = new Intent();
                galnten.setAction(Intent.ACTION_GET_CONTENT);
                galnten.setType("gambar/*");
                startActivityForResult(galnten, GALERY_CODEREQ);
                break;
            case R.id.btnSiman:
                buatAkun();
                break;
        }
    }

    private void buatAkun() {
        mProl.setTitle("Menyimpan Informasi Akun...");
        mProl.show();

        final String user_id = mAuth.getCurrentUser().getUid();
        final String nama = mEditTextNama.getText().toString().trim();
        final String noHp = mEditTextHP.getText().toString().trim();
        final String alamtpsr = mEditTextAlamt.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            mEditTextNama.setError("Harap diisi!");
        } else if (TextUtils.isEmpty(noHp)) {
            mEditTextHP.setError("Harap diisi!");
        } else if (TextUtils.isEmpty(alamtpsr) && resultUri != null) {
            mEditTextAlamt.setError("Ambil Gambar Dahulu");
        } else {
            StorageReference refBaru = stoRef.child(resultUri.getLastPathSegment());
            refBaru.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    String linkFoprof = taskSnapshot.getDownloadUrl().toString();

                    dRefAgen.child(user_id).child("nama_agen").setValue(nama);
                    dRefAgen.child(user_id).child("foto_profile").setValue(linkFoprof);
                    dRefAgen.child(user_id).child("hp_agen").setValue(noHp);
                    dRefAgen.child(user_id).child("agen_pasar").setValue(alamtpsr);
                    mProl.dismiss();
                    startActivity(new Intent(SetupActivity.this, MainActivity.class));
                    Toast.makeText(SetupActivity.this, "Profile Berhasil dibuat", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

//

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_CODEREQ && resultCode == RESULT_OK) {
            myUri = data.getData();

            CropImage.activity(myUri)
                    .setGuidelines(Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                mCircleImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @OnClick(R.id.imgCamera)
    public void onViewClicked() {
    }
}

package com.kelique.allnewfireapp.activity.FirebaseDb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.kelique.allnewfireapp.R;
import com.kelique.allnewfireapp.helper.GalleryUtil;
import com.kelique.allnewfireapp.modelling.Daftar_Teman;
import com.kelique.allnewfireapp.modelling.Unggah_Teman;
import com.kelique.allnewfireapp.modelling.Unggah_Temandua;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class FirebaseDBActivity extends AppCompatActivity {



    public DatabaseReference dRef;
    public StorageReference mIdPhotosStorageReference;
    public FirebaseStorage mFirebaseStorage;
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    String picturePath;
    String id;

    EditText edtNama, edtAlamat;
    Button btn;
    Spinner spin;
    ImageView imgv;
    ListView listViewTeman;
    List<Unggah_Temandua> dftTmn;

    public static final String TEMAN_NM = "nama_teman";
    public static final String TEMAN_ID = "teman_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_db);
        ButterKnife.bind(this);

        edtNama = findViewById(R.id.nameEdit);
        edtAlamat = findViewById(R.id.nameKantor);
        spin = findViewById(R.id.statuSpin);
        btn = findViewById(R.id.btnKirim);
        imgv = findViewById(R.id.circleImage);
        listViewTeman = findViewById(R.id.wadahData);

        dftTmn = new ArrayList<>();

        dRef = FirebaseDatabase.getInstance().getReference("status_teman");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mIdPhotosStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://fireauthentif.appspot.com");
        id = dRef.push().getKey();

        imgv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahData();
            }
        });

        listViewTeman.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Unggah_Temandua tmndu = dftTmn.get(i);
                Intent intan = new Intent(getApplicationContext(), KarirActivity.class);
                intan.putExtra(TEMAN_ID, tmndu.getId());
                intan.putExtra(TEMAN_NM, tmndu.getNama());
                startActivity(intan);
            }
        });

        //TODO: step I updating pilih list item yang akan di update, dan khususnya data tertentu yang akan ditarik

        listViewTeman.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Unggah_Temandua tmnPrtm = dftTmn.get(i);
                bukaDialog(tmnPrtm.getId(), tmnPrtm.getNama(), tmnPrtm.getLinkFoto(), tmnPrtm.getStatus());
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dftTmn.clear();

                for(DataSnapshot temanSS : dataSnapshot.getChildren()){
                    Unggah_Temandua tmm = temanSS.getValue(Unggah_Temandua.class);
                    dftTmn.add(tmm);
                }
                Daftar_Teman adapter = new Daftar_Teman(FirebaseDBActivity.this, dftTmn);
                listViewTeman.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bukaDialog(final String kawanId, final String namaTeman, final String linkTmn, final String statusTmn){
        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.updalete_layout, null);
        diagBuilder.setView(view);

        final TextView txtNamaEdit = view.findViewById(R.id.namaLayout);
        final TextView txtAlamEdit = view.findViewById(R.id.nameKantor);
        final Spinner spinEdit = view.findViewById(R.id.statuSpin);
        final ImageView imgpew = view.findViewById(R.id.circleImage);
        Glide.with(getBaseContext()).load(linkTmn).into(imgpew);

        final Button btnUpdate = view.findViewById(R.id.btnUpdate);
        final Button btnDelete = view.findViewById(R.id.btnDelet);
        diagBuilder.setTitle("Perubahan Atas Nama: ");
        txtNamaEdit.setText(namaTeman);

        final AlertDialog alertDialog = diagBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusEd = spinEdit.getSelectedItem().toString();
                String alamatKtr = txtAlamEdit.getText().toString();


                if(TextUtils.isEmpty(alamatKtr)){
                    txtAlamEdit.setError("Lakukan perubahan Data");
                    return;
                }


                updateData(kawanId, namaTeman, statusEd, alamatKtr, linkTmn);
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDatca(kawanId);
            }
        });


    }

    private void deleteDatca(String kawanId) {
        DatabaseReference dtmnUtama = FirebaseDatabase.getInstance().getReference("status_teman").child(kawanId);
        DatabaseReference dtmnTurunan = FirebaseDatabase.getInstance().getReference("karir_teman").child(kawanId);

        dtmnUtama.removeValue();
        dtmnTurunan.removeValue();

        Toast.makeText(this, "Data Berhasil di hapus", Toast.LENGTH_SHORT).show();
    }

    //TODO : APAKAH INI HARUS URUT (iya betul ini jangan sampai tertukar dengan posisi awalnya) Ok OK
    private boolean updateData(String id, String nama,  String status, String alamatKtr, String linkFoto) {
        DatabaseReference drefUpdate = FirebaseDatabase.getInstance().getReference("status_teman").child(id);

        Unggah_Temandua tmnUpdate = new Unggah_Temandua(id, nama, status, alamatKtr, linkFoto);

        drefUpdate.setValue(tmnUpdate);

        Toast.makeText(this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();

        return true;

    }

    private void tambahData() {

        String nama = edtNama.getText().toString();
        String spina = spin.getSelectedItem().toString();
        String lamat = edtAlamat.getText().toString();

        if(TextUtils.isEmpty(nama)){
            edtNama.setError("harus diisi");
        }else if(TextUtils.isEmpty(lamat)){
            edtAlamat.setError("harus diisi");
        }else {
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyHHmmss");
            Date tgl = new Date();
            String namaFile = dateFormat.format(tgl)+".jpg";

            StorageReference imageRef = mIdPhotosStorageReference.child("foto_diri/"+ namaFile);
            imgv.setDrawingCacheEnabled(true);
            imgv.buildDrawingCache();
            Bitmap bitmap = imgv.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] fotoDiri = baos.toByteArray();

            //TODO: buat upload animasi progress bar
            final Unggah_Teman ciri = new Unggah_Teman(id, nama, spina, lamat);
            UploadTask uplod = imageRef.putBytes(fotoDiri);
            uplod.addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    String linkFoto = taskSnapshot.getDownloadUrl().toString();
                    dRef.child(id).setValue(ciri);
                    dRef.child(id).child("linkFoto").setValue(linkFoto);
                    edtNama.setText("");
                    edtAlamat.setText("");
                    Toast.makeText(FirebaseDBActivity.this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                }
            });
        }

        }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                performCrop(picturePath);
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                imgv.setImageBitmap(selectedBitmap);
                imgv.setScaleType(ScaleType.FIT_XY);
            }
        }
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "HP Anda tidak mendukung pemotongan gambar";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}

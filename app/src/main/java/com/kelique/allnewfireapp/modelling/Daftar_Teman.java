package com.kelique.allnewfireapp.modelling;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kelique.allnewfireapp.R;

import java.util.List;

/**
 * Created by kelique on 1/29/2018.
 */

public class Daftar_Teman extends ArrayAdapter<Unggah_Temandua>{

    private Activity context;
    private List<Unggah_Temandua> daftarTemandu;

    public Daftar_Teman(Activity context, List<Unggah_Temandua> daftarTemandu){
        super(context, R.layout.list_view_layout, daftarTemandu);
        this.context = context;
        this.daftarTemandu = daftarTemandu;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.list_view_layout, null, true);

        TextView txtName = listviewItem.findViewById(R.id.txtNamaTeman);
        TextView txtStatus = listviewItem.findViewById(R.id.txtStatusTeman);
        TextView txtAlam = listviewItem.findViewById(R.id.txtAlamTmn);
        ImageView imgTmn = listviewItem.findViewById(R.id.circleImage);

        Unggah_Temandua temm = daftarTemandu.get(position);

        txtName.setText(temm.getNama());
        txtStatus.setText(temm.getStatus());
        txtAlam.setText(temm.getAlamatKtr());

        Glide.with(getContext()).load(temm.getLinkFoto()).into(imgTmn);

        return listviewItem;
    }
}

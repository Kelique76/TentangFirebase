package com.kelique.allnewfireapp.modelling;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kelique.allnewfireapp.R;

import java.util.List;

/**
 * Created by kelique on 1/29/2018.
 */

public class Karir_List extends ArrayAdapter<Karir> {

    private Activity context;
    private List<Karir> daftarKarir;

    public Karir_List (Activity context, List<Karir> daftarKarir){
        super(context, R.layout.list_view_karir_layout, daftarKarir);
        this.context = context;
        this.daftarKarir = daftarKarir;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.list_view_karir_layout, null, true);

        TextView txtKarir = listviewItem.findViewById(R.id.txtKarirTeman);
        TextView txtLating = listviewItem.findViewById(R.id.bintang);


        Karir temm = daftarKarir.get(position);

        txtKarir.setText(temm.getJenisKrr());
        txtLating.setText(String.valueOf(temm.getTingkat()));

        return listviewItem;
    }
}

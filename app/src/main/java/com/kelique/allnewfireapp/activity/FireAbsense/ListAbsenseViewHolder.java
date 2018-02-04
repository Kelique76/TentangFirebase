package com.kelique.allnewfireapp.activity.FireAbsense;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelique.allnewfireapp.R;

/**
 * Created by kelique on 2/4/2018.
 */

public class ListAbsenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imgDP, imgTandaOL;
    public TextView namaPegawai;
    public View mView;
    ItemClikListener mItemClikListener;

    public ListAbsenseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        imgDP = mView.findViewById(R.id.circleImage);
        imgTandaOL = mView.findViewById(R.id.imageView2);

        namaPegawai = mView.findViewById(R.id.txtNamaAbsensi);
    }

//    public ListAbsenseViewHolder(View itemView, ItemClikListener itemClikListener) {
//        super(itemView);
//        mItemClikListener = itemClikListener;
//    }

    @Override
    public void onClick(View view) {
        mItemClikListener.onClick(view, getAdapterPosition());
    }
}

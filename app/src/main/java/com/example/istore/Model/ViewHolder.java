package com.example.istore.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.R;


public class ViewHolder extends RecyclerView.ViewHolder {

    TextView pName, pQty, pExp;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClicklistener.onItemClick(v, getAdapterPosition());
            }
        });
        // item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mClicklistener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        // initialize view with item_layout
        pName = itemView.findViewById(R.id.text_view_item_Name);
        pQty  = itemView.findViewById(R.id.text_view_qyt);
        pExp  = itemView.findViewById(R.id.text_view_expiry_date);


    }

    private ClickListener mClicklistener;

    // interface for click listeners
    public  interface  ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ClickListener clickListener){

        mClicklistener = clickListener;
    }


}

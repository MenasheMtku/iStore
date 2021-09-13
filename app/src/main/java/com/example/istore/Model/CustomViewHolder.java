package com.example.istore.Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.R;


public class CustomViewHolder extends RecyclerView.ViewHolder {

    public ImageView pImageView;
    public TextView pName, pQty, pExp, pCategory;
    View mView;

    public CustomViewHolder(@NonNull View itemView) {
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
        pImageView = itemView.findViewById(R.id.prodImageCardIV_ID);
        pName = itemView.findViewById(R.id.prodNameCardTV_ID);
        pCategory =  itemView.findViewById(R.id.itemCategoryTV);
        pQty  = itemView.findViewById(R.id.prodQtyCardTV_ID);
        pExp  = itemView.findViewById(R.id.prodExpiryCardTV_ID);



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

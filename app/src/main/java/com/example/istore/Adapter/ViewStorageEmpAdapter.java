package com.example.istore.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Manager.EditProduct;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class ViewStorageEmpAdapter extends FirestoreRecyclerAdapter<ProdModel, ViewStorageEmpAdapter.ProdHolder> {

    final private Context context;


    public ViewStorageEmpAdapter(@NonNull FirestoreRecyclerOptions<ProdModel> options, Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProdHolder holder, int position, @NonNull ProdModel model) {

        holder.eName.setText(model.getName());
        holder.eCategory.setText(model.getCategory());
        holder.eDesc.setText(model.getDescription());
        holder.eQty.setText(model.getQuantity());
        try {
            Picasso.get().load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24)
                    .into(holder.eImageView);
        }
        catch (Exception e){
            holder.eImageView.
                    setImageResource(R.drawable.ic_outline_no_image_24);

        }


    }
    @NonNull
    @Override
    public ProdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_view_tock_row, parent, false);

        return new ProdHolder(v);
    }



    class ProdHolder extends RecyclerView.ViewHolder {

        private ImageView eImageView;
        private TextView eName, eQty, eCategory, eDesc;

        public ProdHolder(@NonNull View itemView) {
            super(itemView);

            // ui init
            eName      = itemView.findViewById(R.id.nameEmpCardTv);
            eDesc      = itemView.findViewById(R.id.descEmpCardId);
            eQty       = itemView.findViewById(R.id.qtyEmpCardTv);
            eCategory  = itemView.findViewById(R.id.catNameEmpCardTv);
            eImageView = itemView.findViewById(R.id.cardImageEmp_id);
        }
    }
}

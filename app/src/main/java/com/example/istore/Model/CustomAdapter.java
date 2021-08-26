package com.example.istore.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.SK_Activities.AddProduct;
import com.example.istore.R;
import com.example.istore.Viewstock;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Prod> prodList;
    Viewstock viewstock;
    Context context;


    public CustomAdapter(Viewstock viewstock, List<Prod> prodList) {
        this.viewstock = viewstock;
        this.prodList = prodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate Layout
        View iteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(iteView);
        // handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String tName = prodList.get(position).getName();
                String tQty = prodList.get(position).getQuantity();
                String tExp = prodList.get(position).getExpiry();

                Snackbar snackbar = Snackbar.make(view, tQty+ " | " +tName+" | "+ tExp, Snackbar.LENGTH_LONG);
                snackbar.show();
//                Toast.makeText(viewstock, tName+"   "+tQty+"\n"+ tExp,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                // creating alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(viewstock);
                // option to display on dialog
                String[] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            // update is clicked ---> get data
                            String id = prodList.get(position).getId();
                            String name = prodList.get(position).getName();
                            String quantity = prodList.get(position).getQuantity();
                            String expiry = prodList.get(position).getExpiry();

                            // intent to start activity
                            Intent intent = new Intent(viewstock,AddProduct.class);
                            // put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pName", name);
                            intent.putExtra("pQuantity", quantity);
                            intent.putExtra("pExpiry", expiry);
                            // start activity
                            viewstock.startActivity(intent);
                        }
                        if(which == 1){
                            viewstock.deleteData(position);
                        }
                    }
                }).create().show();

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pName.setText(prodList.get(position).getName());
        holder.pQty.setText(prodList.get(position).getQuantity());
        holder.pExp.setText(prodList.get(position).getExpiry());
    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }
}

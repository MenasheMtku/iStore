package com.example.istore.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.SK_Activities.AddProduct;
import com.example.istore.R;
import com.example.istore.Viewstock;
import com.google.android.material.snackbar.Snackbar;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable{

    LocalDate localDate = LocalDate.now();
    private static final String TAG = "items";
    private List<Prod> prodList;
            List<Prod> prodListFull;
            Viewstock viewstock;
            Context context;



    public CustomAdapter(Viewstock viewstock, List<Prod> prodList) {
        this.viewstock = viewstock;
        this.prodList = prodList;
             prodListFull = new ArrayList<>(prodList);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(Prod item : prodList){
            Log.d(TAG, item.getName()+"---->"+item.getExpiry()+" "+localDate.format(formatter));
        }


    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }



    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Prod> filteredList = new ArrayList<>();
            if( charSequence == null || charSequence.length() == 0 ){

                filteredList.addAll(prodListFull);

            }else{
                 String filterPatten = charSequence.toString().toLowerCase().trim();

                 for(Prod item : prodListFull){
                     if(item.getName().toLowerCase().contains(filterPatten)){
                         filteredList.add(item);
                     }
                 }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {

            prodList.clear();
            prodList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
}

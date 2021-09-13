package com.example.istore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.example.istore.Model.Prod;
import com.example.istore.Model.CustomViewHolder;
import com.example.istore.R;
import com.example.istore.SK_Activities.AddProduct;
import com.example.istore.Viewstock;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> implements Filterable{

//    LocalDate todayDate = LocalDate.now();
//    LocalDate futreDate;
    private static final String TAG = "items";
    private static final String TAG1 = "onBindString";
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
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate Layout
        View iteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(iteView);
        // handle item clicks here
        viewHolder.setOnClickListener(new CustomViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String tName = prodList.get(position).getName();
                String tQty = prodList.get(position).getQuantity();
                String tExp = prodList.get(position).getExpiry();
                String tcat = prodList.get(position).getCatName();
                String timage = prodList.get(position).getImageUrl();

                Toast.makeText(viewstock, "Name : "+tName+"\n" +
                                                "Qty : "+tQty+"\n" +
                                                "ExDate : " + tExp+ "\n"+
                                                "Category : "+tcat +"\n"+
                                                "ImagePath : "+timage,
                                                Toast.LENGTH_LONG).show();
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
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // get data
        Prod prod = prodList.get(position);


        String uID = prod.getId();
        String image = prod.getImageUrl();
        String name = prod.getName();
        String cat = prod.getCatName();
        String qty = prod.getQuantity();
        String exp = prod.getExpiry();

//        //testing
//        Log.i(TAG1, "Get Strings before set on holder: \n"+
//                "UUID: "+ uID +"\n"+
//                "Name: "+ name +"\n"+
//                "Quantity: "+ qty +"\n"+
//                "Date: "+ exp +"\n"+
//                "Category: "+ cat +"\n"+
//                "ImageAddress: "+ image +"");

        // set data
        holder.pName.setText(name);
        holder.pCategory.setText(cat);
        holder.pQty.setText(qty);
        holder.pExp.setText(exp);

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic__add_new_prod).into(holder.pImageView);
        }
        catch (Exception e){
            holder.pImageView.setImageResource(R.drawable.ic__add_new_prod);
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Prod item : prodList){

            LocalDate todayDate = LocalDate.now();
            String today = todayDate.format(formatter);
            String futreDate = String.format(item.getExpiry(), formatter);

            LocalDate date_1 = LocalDate.parse(today,formatter);
            LocalDate date_2 = LocalDate.parse(futreDate,formatter);
            // calculate difference between the dates
            long diff = ChronoUnit.DAYS.between(date_1,date_2);

            if(diff <= 0){
                Log.i(TAG,  item.getName()+" Expiry Date: "+futreDate+" EXPIRED!!!");

            }else if( diff <= 14){
                Log.i(TAG, item.getName()+" Expiry Date: "+futreDate+" "+String.valueOf(diff)+" DAYS to expire");

            }
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

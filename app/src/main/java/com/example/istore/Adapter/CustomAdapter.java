//package com.example.istore.Adapter;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.istore.Manager.EditProduct;
//import com.example.istore.Model.ProdModel;
//import com.example.istore.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements Filterable{
//
//    final private Context context;
//    private List<ProdModel> prodList;
//    private List<ProdModel> prodListFull;
//    private FirebaseFirestore db;
////    ProgressDialog pd = new ProgressDialog(viewstock);
//    private static final String TAG = "items";
//    private static final String TAG1 = "onBindCard";
////    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//
//
//    public CustomAdapter(Context context,List<ProdModel> prodList) {
////        this.viewstock = viewstock;
//        this.context = context;
//        this.prodList = prodList;
//             prodListFull = new ArrayList<>(prodList);
//    }
//
//
//    @NonNull
//    @Override
//    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // inflate Layout
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.manger_stock_row, parent, false);
//
//        CustomViewHolder customViewHolder = new CustomViewHolder(v);
//
//        return customViewHolder;
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//
//        ProdModel prod = prodList.get(position);
//
//
//        holder.pName.setText(prod.getName());
//        holder.pCategory.setText(prod.getCategory());
//        holder.pDesc.setText(prod.getDescription());
//        holder.pExp.setText(prod.getExpiry());
//        holder.pQty.setText(prod.getQuantity());
//        try {
//            Glide.with(context).load(prod.getImageUrl())
//                    .placeholder(R.drawable.ic_outline_no_image_24)
//                    .into(holder.pImageView);
//        }
//        catch (Exception e){
//            holder.pImageView.
//                    setImageResource(R.drawable.ic_outline_no_image_24);
//
//        }
//
//        Log.i(TAG1, "Get Strings before set on holder: \n"+
//                "UUID: "+ prod.getId() +"\n"+
//                "Name: "+ prod.getName() +"\n"+
//                "Price: "+ prod.getPrice() +"\n"+
//                "Desc: "+ prod.getDescription() +"\n"+
//                "Quantity: "+ prod.getQuantity() +"\n"+
//                "LastDate: "+ prod.getExpiry() +"\n"+
//                "Category: "+ prod.getCategory()+"\n"+
//                "ImageAddress: "+ prod.getImageUrl() +"\n");
//
//        // get data
////        ProdModel prod = prodList.get(position);
////
////        String uID = prod.getId();
////        String image = prod.getImageUrl();
////        String name = prod.getName();
////        String cat = prod.getCatName();
////        String desc = prod.getDescription();
////        String price = prod.getPrice();
////        String exp = prod.getExpiry();
////        String qty = prod.getQuantity();
//
//
//
//
//        holder.itemView.setOnClickListener(new  View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                detailsBottomSheet(prod);
//            }
//        });
//
//
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////        for(ProdModel item : prodList){
////
////            LocalDate todayDate = LocalDate.now();
////            String today = todayDate.format(formatter);
////            String futreDate = String.format(item.getExpiry(), formatter);
////
////            LocalDate date_1 = LocalDate.parse(today,formatter);
////            LocalDate date_2 = LocalDate.parse(futreDate,formatter);
////            // calculate difference between the dates
////            long diff = ChronoUnit.DAYS.between(date_1,date_2);
////
////            if(diff <= 0){
////                Log.i(TAG,  item.getName()+" Expiry Date: "+futreDate+" EXPIRED!!!");
////
////            }else if( diff <= 14){
////                Log.i(TAG, item.getName()+" Expiry Date: "+futreDate+" "+String.valueOf(diff)+" DAYS to expire");
////
////            }
////        }
//    }
//
//    private void detailsBottomSheet(ProdModel prod) {
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_details_admin,null);
//        //set views to BottomSheet
//        bottomSheetDialog.setContentView(view);
//        // init views of BottomSheet
//        ImageButton backButton = view.findViewById(R.id.backBtn);
//        ImageButton deleteButton = view.findViewById(R.id.deleteBtn);
//        ImageButton editButton = view.findViewById(R.id.editBtn);
//        ImageView   prodImage = view.findViewById(R.id.productImageIV);
//        TextView    prodName = view.findViewById(R.id.prodNameTV);
//        TextView    prodCategory = view.findViewById(R.id.prodCategoryTV);
////        TextView    prodPrice = view.findViewById(R.id.prodPriceTV);
//        TextView    prodQuantity = view.findViewById(R.id.prodQuantityTV);
//        TextView    prodExpiryDate = view.findViewById(R.id.prodExpiryTV);
//        TextView    prodDesc = view.findViewById(R.id.prodDescriptionTV);
//
//        // get data
//        String uID = prod.getId();
//        String image = prod.getImageUrl();
//        String name = prod.getName();
////        String price = prod.getPrice();
//        String cat = prod.getCategory();
////        String qty = prod.getQuantity();
//        String exp = prod.getExpiry();
//        String desc = prod.getDescription();
//
//        //set data
//        prodName.setText("Name: "+prod.getName());
//        prodCategory.setText("Category: "+prod.getCategory());
//        prodDesc.setText("Desc: "+prod.getDescription());
//        prodExpiryDate.setText("Last Date: "+prod.getExpiry());
////        prodPrice.setText("Price: "+prod.getPrice());
//        prodQuantity.setText("Quantity: "+prod.getQuantity());
//
//
//        try {
//            Glide.with(context).load(prod.getImageUrl())
//                    .placeholder(R.drawable.ic_outline_no_image_24).into(prodImage);
//        }
//        catch (Exception e){
//            prodImage.setImageResource(R.drawable.ic_outline_no_image_24);
//        }
//
//        bottomSheetDialog.show();
//
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.dismiss();
//                // open edit product activity
//                Intent intent  = new Intent(context, EditProduct.class);
//                intent.putExtra("productId", uID);
//                context.startActivity(intent);
//
//            }
//        });
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottomSheetDialog.dismiss();
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Delete")
//                        .setMessage("You confirm to delete " + name+" ?")
//                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                deleteProduct(uID);
//                            }
//                        })
//                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//    }
//
//    private void deleteProduct(String uID) {
//
//        // set title of progressDialog
////        pd.setTitle("Deleting Product...");
//        // show progressDialog
////        pd.show();
//        // access product location and delete it
//        db = FirebaseFirestore.getInstance();
//        db.collection("Products").document(uID)
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
////                        pd.dismiss();
//                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
////                        prodList.notify();
////                        showProducts();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                        pd.dismiss();
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return prodList.size();
//    }
//
//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }
//
//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            List<ProdModel> filteredList = new ArrayList<>();
//            if( charSequence == null || charSequence.length() == 0 ){
//
//                filteredList.addAll(prodListFull);
//
//            }else{
//                 String filterPatten = charSequence.toString().toLowerCase().trim();
//
//                 for(ProdModel item : prodListFull){
//                     if(item.getName().toLowerCase().contains(filterPatten)){
//                         filteredList.add(item);
//                     }
//                 }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults results) {
//
//            prodList.clear();
//            prodList.addAll((List)results.values);
//            notifyDataSetChanged();
//
//        }
//    };
//
//    public class CustomViewHolder extends RecyclerView.ViewHolder{
//
//        private ImageView pImageView;
//        private TextView pName, pQty, pExp, pCategory, pDesc;
//
//        public CustomViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            // ui init
//            pName      = itemView.findViewById(R.id.prodName_ID);
////            pDesc      = itemView.findViewById(R.id.prodDescId);
//            pCategory  = itemView.findViewById(R.id.itemCategoryTV);
//            pQty       = itemView.findViewById(R.id.prodQtyCardTV_ID);
//            pExp       = itemView.findViewById(R.id.prodExpiryCardTV_ID);
//            pImageView = itemView.findViewById(R.id.cardImage_id);
//
//        }
//    }
//
//}

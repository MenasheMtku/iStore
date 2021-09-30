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

import com.example.istore.EditProduct;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class ViewStockAdapter extends FirestoreRecyclerAdapter<ProdModel,ViewStockAdapter.ProdHolder> {

    final private Context context;
    private static final String TAG1 = "onBind";


//    Viewstock viewstock;
    public ViewStockAdapter(@NonNull FirestoreRecyclerOptions<ProdModel> options, Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProdHolder holder, int position, @NonNull ProdModel model) {

        holder.pName.setText(model.getName());
        holder.pCategory.setText(model.getCategory());
        holder.pDesc.setText(model.getDescription());
        holder.pExp.setText(model.getExpiry());
        holder.pQty.setText(model.getQuantity());
        try {
            Picasso.get().load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24)
                    .into(holder.pImageView);
        }
        catch (Exception e){
            holder.pImageView.
                    setImageResource(R.drawable.ic_outline_no_image_24);

        }
        Log.i(TAG1, "Get Strings before set on holder: \n"+
                "UUID: "+ model.getId() +"\n"+
                "Name: "+ model.getName() +"\n"+
                "Price: "+ model.getPrice() +"\n"+
                "Desc: "+ model.getDescription() +"\n"+
                "Quantity: "+ model.getQuantity() +"\n"+
                "LastDate: "+ model.getExpiry() +"\n"+
                "Category: "+ model.getCategory()+"\n"+
                "ImageAddress: "+ model.getImageUrl() +"\n");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBottomSheet(model);
            }
        });

    }
    @NonNull
    @Override
    public ProdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new ProdHolder(v);
    }

    private void detailsBottomSheet(ProdModel model) {

//        Viewstock viewstock;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bs_product_details_admin,null);
        //set views to BottomSheet
        bottomSheetDialog.setContentView(view);
        // init views of BottomSheet
        ImageButton backButton = view.findViewById(R.id.backBtn);
        ImageButton deleteButton = view.findViewById(R.id.deleteBtn);
        ImageButton editButton = view.findViewById(R.id.editBtn);
        ImageView   prodImage = view.findViewById(R.id.productImageIV);
        TextView    prodName = view.findViewById(R.id.prodNameTV);
        TextView    prodCategory = view.findViewById(R.id.prodCategoryTV);
        TextView    prodPrice = view.findViewById(R.id.prodPriceTV);
        TextView    prodQuantity = view.findViewById(R.id.prodQuantityTV);
        TextView    prodExpiryDate = view.findViewById(R.id.prodExpiryTV);
        TextView    prodDesc = view.findViewById(R.id.prodDescriptionTV);

        // get data
          String uID = model.getId();
//        String image = model.getImageUrl();
          String name = model.getName();
//        String price = model.getPrice();
//        String cat = model.getCategory();
//        String qty = model.getQuantity();
//        String exp = model.getExpiry();
//        String desc = model.getDescription();

        //set data
        prodName.setText("Name: "+model.getName());
        prodCategory.setText("Category: "+model.getCategory());
        prodDesc.setText("Desc: "+model.getDescription());
        prodExpiryDate.setText("Last Date: "+model.getExpiry());
        prodPrice.setText("Price: "+model.getPrice());
        prodQuantity.setText("Quantity: "+model.getQuantity());


        try {
            Picasso.get().load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24).into(prodImage);
        }
        catch (Exception e){
            prodImage.setImageResource(R.drawable.ic_outline_no_image_24);
        }

        bottomSheetDialog.show();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                // open edit product activity
                Intent intent  = new Intent(context, EditProduct.class);
                intent.putExtra("productId", uID);
                context.startActivity(intent);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("You confirm to delete " + name+" ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteProduct(uID);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }




    private void deleteProduct(String uID) {


    }


    class ProdHolder extends RecyclerView.ViewHolder {

        private ImageView pImageView;
        private TextView pName, pQty, pExp, pCategory, pDesc;

        public ProdHolder(@NonNull View itemView) {
            super(itemView);

            // ui init
            pName      = itemView.findViewById(R.id.prodName_ID);
            pDesc      = itemView.findViewById(R.id.prodDescId);
            pCategory  = itemView.findViewById(R.id.itemCategoryTV);
            pQty       = itemView.findViewById(R.id.prodQtyCardTV_ID);
            pExp       = itemView.findViewById(R.id.prodExpiryCardTV_ID);
            pImageView = itemView.findViewById(R.id.cardImage_id);
        }
    }
}

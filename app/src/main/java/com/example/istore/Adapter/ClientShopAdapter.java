package com.example.istore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ClientShopAdapter extends FirestoreRecyclerAdapter<ProdModel,ClientShopAdapter.ShopHolder> {

        final private Context context;

        int totalQuantity = 1;
        int totalPrice = 0;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClientShopAdapter(@NonNull FirestoreRecyclerOptions<ProdModel> options,Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopHolder holder, int position, @NonNull ProdModel model) {

        holder.shopProdName.setText(model.getName());
        holder.shopProdDesc.setText(model.getDescription());
        holder.shopProdPrice.setText(model.getPrice());
        try {
            Glide.with(context)
                    .load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24)
                    .into(holder.shopProdImage);
        }
        catch (Exception e){
            holder.shopProdImage.setImageResource(R.drawable.ic_outline_no_image_24);

        }

        holder.shopAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: Add to cart was clicked");

                prodDeatilsCartBottomSheet(model);;

            }
        });


    }

    private void prodDeatilsCartBottomSheet(ProdModel model) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bs_cart_product_detail,null);
        //set views to BottomSheet
        bottomSheetDialog.setContentView(view);

        // init views of BottomSheet
        ImageView   prodImage = view.findViewById(R.id.pImageCartDetailIV);
        TextView    prodName = view.findViewById(R.id.pNameCartDetaitTV);
        TextView    prodDescription = view.findViewById(R.id.pDescCartDetaiTV);
        TextView    prodPrice = view.findViewById(R.id.pPriceCartDetailTv);
        TextView    qtyInStock = view.findViewById(R.id.pQtyInStockCartDetailTv);
        TextView    prodQuantity = view.findViewById(R.id.pQtyCartDetailsTv);

        ImageButton upBtn = view.findViewById(R.id.increamentBtn);
        ImageButton downBtn = view.findViewById(R.id.decreamentBtn);

        Button addToCartBtn = view.findViewById(R.id.addToCartBtn);
        Button buyNowBtn       = view.findViewById(R.id.buyNowBtn);

        //set data
        prodName.setText(model.getName());
        prodDescription.setText("Description: "+model.getDescription());
        qtyInStock.setText("Availble: "+model.getQuantity());
        prodPrice.setText("Price Per Unit: $"+model.getPrice());
//        prodQuantity.setText(model.getQuantity());

        try {
            Glide.with(context).
                    load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24)
                    .into(prodImage);
        }
        catch (Exception e){
            prodImage.setImageResource(R.drawable.ic_outline_no_image_24);
        }

        bottomSheetDialog.show();

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity < 10 ){
                    totalQuantity++;
                    prodQuantity.setText(String.valueOf(totalQuantity));

                }
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1 ){
                    totalQuantity--;
                    prodQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Add item was clicked", Toast.LENGTH_SHORT).show();
                addToCart(model);
            }
        });
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Buy now item was clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addToCart(ProdModel model) {

        String saveCurrentTime ,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        totalPrice = Integer.parseInt(model.getPrice())*totalQuantity;

        final HashMap <String, Object> cartMap = new HashMap<>();

        cartMap.put("name", model.getName());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("price", model.getPrice());
        cartMap.put("totalQuantity",String.valueOf(totalQuantity));
        cartMap.put("totalPrice",String.valueOf(totalPrice));

        FirebaseAuth vAuth;
        vAuth = FirebaseAuth.getInstance();
        // db declaration
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartReff = db.collection("AddToCart");

        cartReff.document(vAuth.getCurrentUser().getUid())
                .collection("User")
                .add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(context, "Added To Cart",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_row, parent, false);

        return new ClientShopAdapter.ShopHolder(v);

    }

    public class ShopHolder extends RecyclerView.ViewHolder {

        // ui views
        private ImageView shopProdImage;
        private TextView shopProdName;
        private TextView  shopProdDesc;
        private TextView  shopAddToCart;
        private TextView  shopProdPrice;

        public ShopHolder(@NonNull View itemView) {
            super(itemView);

            // ui init
            // image
            shopProdImage =  itemView.findViewById(R.id.shopProdImageId);
            // text
            shopProdName  =  itemView.findViewById(R.id.shopProdNameId);
            shopProdDesc  =  itemView.findViewById(R.id.shopProdDescId);
            shopProdPrice =  itemView.findViewById(R.id.shopProdPriceTv);
            // button
            shopAddToCart =  itemView.findViewById(R.id.shopAddToCartTv);
        }
    }
}

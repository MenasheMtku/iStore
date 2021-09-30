package com.example.istore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.example.istore.UserShop;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ClientShopAdapter extends FirestoreRecyclerAdapter<ProdModel,ClientShopAdapter.ShopHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     * @param userShop
     */
    public ClientShopAdapter(@NonNull FirestoreRecyclerOptions<ProdModel> options, UserShop userShop) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopHolder holder, int position, @NonNull ProdModel model) {

        holder.shopProdName.setText(model.getName());
        holder.shopProdDesc.setText(model.getDescription());
        holder.shopProdPrice.setText(model.getPrice());
        try {
            Picasso.get().load(model.getImageUrl())
                    .placeholder(R.drawable.ic_outline_no_image_24).into(holder.shopProdImage);
        }
        catch (Exception e){
            holder.shopProdImage.setImageResource(R.drawable.ic_outline_no_image_24);

        }


    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card, parent, false);

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

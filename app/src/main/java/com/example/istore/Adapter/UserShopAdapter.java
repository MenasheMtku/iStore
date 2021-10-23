//package com.example.istore.Adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.istore.Model.ProdModel;
//import com.example.istore.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserShopAdapter extends RecyclerView.Adapter<UserShopAdapter.UserProduct> implements Filterable {
//
//    private Context context;
//    private ArrayList<ProdModel> shopProdList;
//    private ArrayList<ProdModel> fullShopProdList;
//
//    public UserShopAdapter(Context context, ArrayList<ProdModel> shopProdList) {
//        this.context = context;
//        this.shopProdList = shopProdList;
//            fullShopProdList = new ArrayList<>(shopProdList);
//    }
//
//    @NonNull
//    @Override
//    public UserProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View iteView;
//        iteView = LayoutInflater.from(context).inflate(R.layout.shop_row, parent, false);
//
//        return new UserProduct(iteView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserProduct holder, int position) {
//
//        // get data
//        ProdModel prod = shopProdList.get(position);
//
//        holder.shopProdName.setText(prod.getName());
//        holder.shopProdDesc.setText(prod.getDescription());
//        holder.shopProdPrice.setText(prod.getPrice());
//        try {
//            Glide.with(context)
//                    .load(prod.getImageUrl())
//                    .placeholder(R.drawable.ic_outline_no_image_24)
//                    .into(holder.shopProdImage);
//        }
//        catch (Exception e){
//            holder.shopProdImage.setImageResource(R.drawable.ic_outline_no_image_24);
//
//        }
//
//        holder.shopAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Add product to cart
//
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // show product details
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return shopProdList.size();
//    }
//
//    @Override
//    public Filter getFilter() {
//        return userShopFilter;
//    }
//
//    private Filter userShopFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//
//            ArrayList<ProdModel> filteredList = new ArrayList<>();
//            if( charSequence == null || charSequence.length() == 0 ){
//
//                filteredList.addAll(fullShopProdList);
//
//            }else{
//                String filterPatten = charSequence.toString().toLowerCase().trim();
//
//                for(ProdModel item : fullShopProdList){
//                    if(item.getName().toLowerCase().contains(filterPatten)){
//                        filteredList.add(item);
//                    }
//                }
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
//            shopProdList.clear();
//            shopProdList.addAll((List)results.values);
//            notifyDataSetChanged();
//        }
//    };
//
//    class UserProduct extends RecyclerView.ViewHolder{
//
//        // ui views
//        private ImageView shopProdImage;
//        private TextView  shopProdName;
//        private TextView  shopProdDesc;
//        private TextView  shopAddToCart;
//        private TextView  shopProdPrice;
//
//        public UserProduct(@NonNull View itemView) {
//            super(itemView);
//                              // ui init
//            // image
//            shopProdImage =  itemView.findViewById(R.id.shopProdImageId);
//            // text
//            shopProdName  =  itemView.findViewById(R.id.shopProdNameId);
//            shopProdDesc  =  itemView.findViewById(R.id.shopProdDescId);
//            shopProdPrice =  itemView.findViewById(R.id.shopProdPriceTv);
//            // button
//            shopAddToCart =  itemView.findViewById(R.id.shopAddToCartTv);
//
//
//        }
//    }
//}

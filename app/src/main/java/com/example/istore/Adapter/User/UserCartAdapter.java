package com.example.istore.Adapter.User;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Model.CartModel;
import com.example.istore.R;

import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.CartHolder> {

     private Context context;
     private List<CartModel> cartList;
     int totalAmount = 0;
     float holderPrice = 0.0F;

    public UserCartAdapter(Context context,List<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_cart_row, parent, false);

        return new UserCartAdapter.CartHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        CartModel model = cartList.get(position);

        holder.cartCurrDate.setText(model.getDate()+", "+model.getTime());
//        holder.cartCurrTime.setText(model.getTime());
        holder.cartPname.setText(model.getName());
        holder.cartPprice.setText(model.getPrice());
        holder.cartTotalQty.setText(model.getTotalQuantity());
        holder.cartTotalPrice.setText(model.getTotalPrice()+"$");

//        Log.i("TAG1", "Get Strings before set on holder: \n"+
//                "date: "+ model.getDate() +"\n"+
//                "time: "+ model.getTime() +"\n"+
//                "name: "+ model.getName() +"\n"+
//                "price: "+ model.getPrice() +"\n"+
//                "total quantity: "+ model.getTotalQuantity() +"\n"+
//                "total price: "+ model.getTotalPrice());

        // pass cart total amount to userCart activity
        // calculate the Customer overall pay
        holderPrice = Float.parseFloat(model.getTotalPrice());
        totalAmount = totalAmount + Math.round(holderPrice);

        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmount);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder {

        private TextView  cartCurrDate;
//        private TextView  cartCurrTime;
        private TextView  cartPname;
        private TextView  cartPprice;
        private TextView  cartTotalQty;
        private TextView  cartTotalPrice;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            // ui init()
            cartCurrDate   =  itemView.findViewById(R.id.current_date);
            cartPname      =  itemView.findViewById(R.id.product_name);
            cartPprice     =  itemView.findViewById(R.id.product_price);
            cartTotalQty   =  itemView.findViewById(R.id.total_quantity);
            cartTotalPrice =  itemView.findViewById(R.id.total_price);

        }
    }
}

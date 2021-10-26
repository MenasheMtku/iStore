package com.example.istore.Adapter.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Manager.OrderDetails;
import com.example.istore.Model.AddressModel;
import com.example.istore.Model.CheckoutModel;
import com.example.istore.R;
import com.example.istore.User.UserAddress;
import com.example.istore.User.UserCart;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ViewOrdersAdapter extends FirestoreRecyclerAdapter<CheckoutModel, ViewOrdersAdapter.OrderHolder> {

    final private Context context;

    public ViewOrdersAdapter(@NonNull FirestoreRecyclerOptions<CheckoutModel> options, Context context) {

        super(options);
        this.context = context;


    }
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manger_order_row, parent, false);


        return new OrderHolder(v);
    }
    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull CheckoutModel model) {

        String order_id = model.getOrder_id();
        String user_id = model.getUser_id();
        holder.oName.setText(model.getUser_name());
        holder.oTime.setText(model.getOrder_date());
        holder.oAddress.setText(model.getShips_to());
        holder.oTotal.setText(model.getTotal_items());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send user id and order id
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);

            }
        });
    }
    class OrderHolder extends RecyclerView.ViewHolder {

        private TextView oName, oTime , oTotal, oAddress;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            // ui init
            oName    = itemView.findViewById(R.id.customer_id);
            oTime    = itemView.findViewById(R.id.order_time);
            oTotal   = itemView.findViewById(R.id.totalItems_ID);
            oAddress = itemView.findViewById(R.id.custAddressTv_id);


        }
    }
}

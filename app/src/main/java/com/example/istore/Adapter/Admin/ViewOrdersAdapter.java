package com.example.istore.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Model.AddressModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ViewOrdersAdapter extends FirestoreRecyclerAdapter<AddressModel, ViewOrdersAdapter.OrderHolder> {

    final private Context context;

    public ViewOrdersAdapter(@NonNull FirestoreRecyclerOptions<AddressModel> options, Context context) {

        super(options);
        this.context = context;


    }
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new OrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manger_order_row, parent,false));
    }
    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull AddressModel model) {

        holder.oName.setText(model.getName());
        holder.oTime.setText(String.format("%s, %s", model.getDate(), model.getTime()));
        holder.oAddress.setText(model.getAddress());
        holder.oTotal.setText(model.getTotalPrice());
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

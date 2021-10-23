package com.example.istore.Adapter.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Model.AddressModel;
import com.example.istore.R;

import java.util.List;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.ViewHolder> {

    private Context context;
    private List<AddressModel> addressModelsList;
    SelectAddress selectAddress;
    private RadioButton selectedRadioBtn;

    public UserAddressAdapter(Context context, List<AddressModel> addressModelsList, SelectAddress selectAddress) {
        this.context = context;
        this.addressModelsList = addressModelsList;
        this.selectAddress = selectAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_address_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.address.setText(addressModelsList.get(position).getAddress());
        holder.name.setText(addressModelsList.get(position).getName());
        holder.phone.setText(addressModelsList.get(position).getPhone());
        holder.psCode.setText(addressModelsList.get(position).getPostalCode());

        holder.selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(AddressModel address: addressModelsList){
                    address.setSelected(false);
                }
                addressModelsList.get(position).setSelected(true);

                if(selectedRadioBtn != null){
                    selectedRadioBtn.setChecked(false);
                }

                selectedRadioBtn = (RadioButton) view;
                selectedRadioBtn.setChecked(true);
                selectAddress.setAddress(addressModelsList.get(position).getAddress());


            }
        });

    }

    @Override
    public int getItemCount() {
        return addressModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  address;
        private TextView  name;
        private TextView  phone;
        private TextView  psCode;
        private RadioButton selectAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address  =  itemView.findViewById(R.id.address_add);
            name  =  itemView.findViewById(R.id.name_add);
            phone  =  itemView.findViewById(R.id.phone_add);
            psCode  =  itemView.findViewById(R.id.ps_code_add);
            selectAddress  =  itemView.findViewById(R.id.select_address);
        }
    }

    public  interface SelectAddress{
        void setAddress (String address);
    }
}

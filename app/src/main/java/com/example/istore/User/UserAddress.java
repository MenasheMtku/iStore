package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.istore.Adapter.User.UserAddressAdapter;
import com.example.istore.Model.AddressModel;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAddress extends AppCompatActivity implements UserAddressAdapter.SelectAddress {

    Toolbar toolbar;
    RecyclerView uRecyclerView;
    Button continueToPaymentBtn;
    FloatingActionButton fab;

    FirebaseAuth uAuth;
    FirebaseFirestore db;

    private List<AddressModel> addressModelsList;
    private UserAddressAdapter uAdapter;

    String mAddress = "";
    String totalToPay = "";
    String totalItems = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);

        uAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Users")
                .document(uAuth.getCurrentUser().getUid()).collection("Address");
        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uRecyclerView = findViewById(R.id.address_recycler);
//        addAddressBtn  = findViewById(R.id.add_address_btn);
        fab = findViewById(R.id.addNewAddresFabId);
        continueToPaymentBtn  = findViewById(R.id.payment_btn);

        uRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelsList = new ArrayList<>();
        uAdapter = new UserAddressAdapter(getApplicationContext(),addressModelsList,this);
        uRecyclerView.setAdapter(uAdapter);

//        firestore.collection("CurrentUser")
//                .document(uAuth.getCurrentUser().getUid())
//                .collection("Address")
                reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot doc :task.getResult().getDocuments()) {
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelsList.add(addressModel);
                                uRecyclerView.setAdapter(uAdapter);
                            }
                        }
                    }
                });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value1 = extras.getString("total");
            String value2 = extras.getString("items");
            totalToPay = value1;
            totalItems = value2;
            //The key argument here must match that used in the other activity
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserAddress.this, AddAddress.class));
            }
        });
        continueToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shippingAddressSelected = mAddress;
                if(mAddress.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Choose or create Address", Toast.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(UserAddress.this, UserPayment.class);

                    i.putExtra("address",shippingAddressSelected);
                    i.putExtra("total",totalToPay);
                    i.putExtra("items",totalItems);


                    Log.i("TAG", "onClick: "+ totalItems+" "+totalToPay);
                    startActivity(i);
                }

            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress =  address;

        Log.i("TAG", "Address selected: "+mAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.i("TAG", "Address selected: "+mAddress);
    }
}
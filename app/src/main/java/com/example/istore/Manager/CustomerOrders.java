package com.example.istore.Manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Adapter.Admin.ViewOrdersAdapter;
import com.example.istore.Model.CheckoutModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerOrders extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView oRecyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference checkout_ref =  firestore.collection("Checkout");
    private ViewOrdersAdapter ordersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        toolbar = findViewById(R.id.viewOrdersTb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        oRecyclerView = findViewById(R.id.viewOrdersRv);






        loadAllOrders();


    }

    private void loadAllOrders() {
        Query query = checkout_ref.orderBy("order_date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CheckoutModel> options = new FirestoreRecyclerOptions
                .Builder<CheckoutModel>()
                .setQuery(query,CheckoutModel.class)
                .build();
        ordersAdapter = new ViewOrdersAdapter(options, this);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                oRecyclerView = findViewById(R.id.viewOrdersRv);
                oRecyclerView.setHasFixedSize(true);
                oRecyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrders.this));
                oRecyclerView.setAdapter(ordersAdapter);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ordersAdapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();

        ordersAdapter.stopListening();
    }
}
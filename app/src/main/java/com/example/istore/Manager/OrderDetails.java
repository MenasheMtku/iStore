package com.example.istore.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.istore.Adapter.Admin.ViewOrderDetailAdapter;
import com.example.istore.Model.CartModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderDetails extends AppCompatActivity {
    private static final String TAG = "OrderDetails";
    Toolbar toolbar;
    RecyclerView odRecyclerView;
    FirebaseFirestore firestore ;

    private ViewOrderDetailAdapter oDetailAdapter;
    String orderID = "", userID ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oreder_details);

        firestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.viewOrdersDetailsTb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        odRecyclerView = findViewById(R.id.view_order_det_Rv);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderID = extras.getString("order_id");
            userID = extras.getString("user_id");
        }

        Toast.makeText(OrderDetails.this, "ORDER ID = "+orderID+"\nUSER ID"+ userID, Toast.LENGTH_LONG).show();
        Log.i(TAG, "\nOrder Details:"+"\nORDER ID = " + orderID+"\n"+"USER ID = " +userID);

        loadOrderDetails();
    }

    private void loadOrderDetails() {
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userID)
                .collection("AddToCart")
                .whereEqualTo("userID",userID)
                .orderBy("time");


        FirestoreRecyclerOptions<CartModel> options = new FirestoreRecyclerOptions
                .Builder<CartModel>()
                .setQuery(query,CartModel.class)
                .build();
        oDetailAdapter = new ViewOrderDetailAdapter(options, this);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                odRecyclerView = findViewById(R.id.view_order_det_Rv);
                odRecyclerView.setHasFixedSize(true);
                odRecyclerView.setLayoutManager(new LinearLayoutManager(OrderDetails.this));
                odRecyclerView.setAdapter(oDetailAdapter);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (oDetailAdapter != null){
            oDetailAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        oDetailAdapter.stopListening();
    }
}
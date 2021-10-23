package com.example.istore.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.istore.Adapter.Admin.ViewOrdersAdapter;
import com.example.istore.Adapter.Admin.ViewStockAdapter;
import com.example.istore.Employee.EmployeeDashboard;
import com.example.istore.Model.AddressModel;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.example.istore.User.UserShop;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerOrders extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView oRecyclerView;

    private FirebaseFirestore firestore;
    private FirebaseAuth oAuth;

    private ViewOrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        toolbar = findViewById(R.id.viewOrdersTb);
        oRecyclerView = findViewById(R.id.viewOrdersRv);

        oAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users");


        loadAllOrders();


    }

    private void loadAllOrders() {
           CollectionReference ref =  firestore.collection("Users")
                   .document()
                   .collection("AddToCart");

//            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                Query query = ref.orderBy("isUser");
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if(documentSnapshot.getString("isUser") != null) {
//
//                    FirestoreRecyclerOptions<AddressModel> options = new FirestoreRecyclerOptions
//                            .Builder<AddressModel>()
//                            .setQuery(query, AddressModel.class)
//                            .build();
//                    ordersAdapter = new ViewOrdersAdapter(options, this);
//
//                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            oRecyclerView.setAdapter(ordersAdapter);
////                            progressDialog.dismiss();
//
//                        }
//                    });
//                }
//            }
//        });
    }
}
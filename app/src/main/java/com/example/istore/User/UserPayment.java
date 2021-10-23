package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.istore.Login;
import com.example.istore.Model.CartModel;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class UserPayment extends AppCompatActivity {

    private static final String  TAG =  "UserPayment";
    TextView subtotal ,shippingAddress, totalItems, totalFee;
    Button checkOutBtn;

    FirebaseFirestore db;
    FirebaseAuth pAuth;


    String total_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        // firebase init
        db = FirebaseFirestore.getInstance();
        pAuth = FirebaseAuth.getInstance();
//        CollectionReference cartRef = db.collection("Users")
//                .document(pAuth.getCurrentUser().getUid())
//                .collection("AddToCart");


        // ui init
        subtotal = findViewById(R.id.sub_total);
        totalFee = findViewById(R.id.total_amt);
        totalItems = findViewById(R.id.total_items);
        shippingAddress = findViewById(R.id.address_tot_tv);
        checkOutBtn = findViewById(R.id.pay_btn);
        checkOutBtn.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.i(TAG, "Bundle: "+ extras.getString("total")
                    .replaceAll("[^0-9],[.],[^0-9]","")+" => " +extras.getString("items"));

//            total_items = extras.getString("items");
            String sub_t = extras.getString("total");
            String ship_t = extras.getString("address");
            String items_t = extras.getString("items");

            subtotal.setText(sub_t.replaceAll("[^0-9],[.],[^0-9]",""));
            totalFee.setText("$ "+sub_t.replaceAll("[^0-9],[.],[^0-9]",""));
            totalItems.setText(items_t);
            shippingAddress.setText(ship_t);
            checkOutBtn.setVisibility(View.VISIBLE);
        }
            checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkout();

            }
        });
    }

    private void checkout() {

        String  total_order_fee = subtotal.getText().toString();
        String  shipping_address = shippingAddress.getText().toString();
        String  total_items_in_order = totalItems.getText().toString();

        checkoutDialog();


        Log.i(TAG, "checkout: "
                                 +"\nTOTAL ITEMS  ==> "+ total_items_in_order +"\n"
                                 +"USER ADDRESS ==>" + shipping_address+"\n"
                                 +"TOTAL TO PAY ==>"  +total_order_fee);


        total_order_fee = subtotal.getText().toString();
        shipping_address = shippingAddress.getText().toString();


    }

    private void checkoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserPayment.this);
        builder.setTitle("Confirm Payment");
        // Add the buttons
        builder.setMessage("Select yes to continue")
                .setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                updateUserItemStatus();
                saveCart();


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateUserItemStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("hasPaid", true);

        db.collection("Users")
                .document(pAuth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot docSnap: task.getResult()){

                                Log.i(TAG, docSnap.getId()+" => "+docSnap.getData());
                                docSnap.getReference().set(data, SetOptions.merge());
                                // go back to store
                                startActivity(new Intent(UserPayment.this,UserShop.class));
                                finish();
                            }
                        }
                        else{
                            Log.i(TAG, "Error getting documents: "+task.getException());
                        }
                    }
                });
    }
    private void saveCart() {

    }


}
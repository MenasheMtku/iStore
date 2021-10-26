package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.istore.Login;
import com.example.istore.Model.CartModel;
import com.example.istore.Model.Client;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UserPayment extends AppCompatActivity {

    private static final String  TAG =  "UserPayment";
    final String uniqueID = UUID.randomUUID().toString(); // generate id for every cart checkout
    String userID = "";
    TextView subtotal ,shippingAddress, totalItems, totalFee;
    Button checkOutBtn;

    FirebaseFirestore db;
    FirebaseAuth pAuth;

    Calendar calForDate = Calendar.getInstance();
    String  user_name = "",
            date_and_time="",
            total_order_fee ="",
            shipping_address="",
            total_items_in_order="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        // firebase init
        db = FirebaseFirestore.getInstance();
        pAuth = FirebaseAuth.getInstance();
        userID = pAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Users").document(pAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot != null){
                        Log.i(TAG, "onComplete: USERNAME: "+snapshot.getString("fullName"));
                        user_name = snapshot.getString("fullName");
                    }else{
                        Log.i(TAG, "onComplete: NO SUCH USER");
                    }
                }
                else{
                    Log.i(TAG, "get failed with ", task.getException());
                }
            }
        });

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
        checkoutDialog();
    }

    private void checkoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserPayment.this);
        builder.setTitle("Confirm Payment");
        // Add the buttons
        builder.setMessage("Select YES to continue");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                updateUserItemStatus();
                saveCart();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // go back to store
                        startActivity(new Intent(UserPayment.this,UserShop.class));
                        finish();
                    }
                }, 2000);



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
        data.put("orderID", uniqueID);
        data.put("userID", userID);

        db.collection("Users")
                .document(pAuth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot docSnap: task.getResult()){

                                Log.i(TAG, docSnap.getId()+" => "+docSnap.getData());
                                Log.i(TAG, uniqueID);
                                Log.i(TAG, userID);
                                docSnap.getReference().set(data, SetOptions.merge());

                            }
                        }
                        else{
                            Log.i(TAG, "Error getting documents: "+task.getException());
                        }
                    }
                });
    }
    private void saveCart() {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        total_order_fee = subtotal.getText().toString();
        shipping_address = shippingAddress.getText().toString();
        total_items_in_order = totalItems.getText().toString();
        date_and_time = currentDate.format(calForDate.getTime());

        Log.i(TAG, "Items To Save"
                +"\nTOTAL ITEMS ==> "+ total_items_in_order +"\n"
                +"USER ADDRESS  ==>" + shipping_address+"\n"
                +"TOTAL TO PAY  ==>"  +total_order_fee+"\n"
                +"Date AND TIME ==>"  +date_and_time+"\n"
                +"USER ID       ==>"  +date_and_time+"\n"
                +"USER NAME     ==>"  +user_name+"\n");

        FirebaseUser currentUser  = pAuth.getCurrentUser();
        Date timestamp = new Date(System.currentTimeMillis());
        String orderDate = timestamp.toString();

        Log.i(TAG, "onCreate: "+ orderDate);

        Map<String,Object> cart = new HashMap<>();
        cart.put("total_items", total_items_in_order);
        cart.put("ships_to", shipping_address);
        cart.put("total_amount", total_order_fee);
        cart.put("order_date", date_and_time);
        cart.put("user_name", user_name);
        cart.put("order_id", uniqueID);
        cart.put("user_id", userID);

        DocumentReference checkRef = db.collection("Checkout").document(date_and_time);
        checkRef.set(cart, SetOptions.merge());

        Toast.makeText(UserPayment.this,
                "Congratulation !!!\n"+ "The order was successfully received.",
                Toast.LENGTH_LONG).show();


    }


}
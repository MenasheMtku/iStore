package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.istore.Adapter.User.UserCartAdapter;
import com.example.istore.Model.CartModel;
import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCart extends AppCompatActivity {

    public static final String TAG = "UserCart";
    Toolbar toolbar;
    TextView totalPrice;
    Button buyNow;
    RecyclerView cartRecyclerView;

    List<CartModel> cartModelList;
    UserCartAdapter cartAdapter;

    // firstore & auth
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    int overAllTotalAmount;
    String totalToPay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        toolbar = findViewById(R.id.userCartId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get total amount for user cart
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver,new IntentFilter("MyTotalAmount"));
        totalPrice = findViewById(R.id.totalPriceTv);
        buyNow = findViewById(R.id.buyNowUserCart);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new UserCartAdapter(this,cartModelList);

        firestore.collection("Users")
                 .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                 .collection("AddToCart")
                 .whereEqualTo("hasPaid",false)// check if user paid on this item
                 .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot doc :task.getResult().getDocuments()) {

                                CartModel cartModel = doc.toObject(CartModel.class);
                                Log.i(TAG, "onComplete: "+doc.getId());
                                cartModelList.add(cartModel);
                                cartRecyclerView.setAdapter(cartAdapter);
                                cartAdapter.notifyDataSetChanged();
                            }
                        }
                        if(task.getResult().isEmpty()){

                            emptyCartMessage();
                        }
                    }

                });


//        Log.i("TAG", "onCreate: "+totalToPay);
//        loadCart();\
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCart.this, UserAddress.class);
                intent.putExtra("total", totalToPay);
                intent.putExtra("items", String.valueOf(cartAdapter.getItemCount()));

                Log.i("TAG", "onClick: "+ cartAdapter.getItemCount());
                startActivity(intent);
            }
        });
    }


    private void emptyCartMessage() {

        Toast.makeText(UserCart.this, "Cart is empty", Toast.LENGTH_SHORT).show();
        totalPrice.setText("Your cart is empty !");
        buyNow.setVisibility(View.GONE);
    }


    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            float totalBill = intent.getIntExtra("totalAmount",0);

            totalPrice.setText(" "+totalBill);
            totalToPay = totalPrice.getText().toString().replaceAll("[^0-9],[.],[^0-9]","");
//                    .replaceAll("[^0-9],[.],[^0-9]","");

        }
    };

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
//        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        cartAdapter.stopListening();
    }
}
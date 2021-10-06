package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.istore.Adapter.ClientShopAdapter;
import com.example.istore.Adapter.UserShopAdapter;
import com.example.istore.Login;
import com.example.istore.Model.Categories;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class UserShop extends AppCompatActivity {

//    // Keys
//    private static final String KEY_ID       = "id";
//    private static final String KEY_NAME     = "prodtName";
//    private static final String KEY_DESK     = "prodtDescription";
//    private static final String KEY_PRICE    = "prodPrice";
//    private static final String KEY_CATEGORY = "prodCategory";
//    private static final String KEY_QUANTITY = "prodQuantity";
//    private static final String KEY_IMAGEURI = "prodImageUrl";
//    private static final String KEY_EXPIRY   = "prodExpirationDate";

    private TextView pageTitle , helloUserTv ,categorySelected;
    private ImageButton logoutUser, viewCart;
    private EditText searchProducts;
    private ImageView filterProducts;
    private RecyclerView shopRV;
    RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;

    // db declaration
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference prodReff = db.collection("Products");


    private UserShopAdapter usAdapter;
    private ClientShopAdapter shopAdapter;

    private ArrayList<ProdModel> prodList = new ArrayList<>();

    private ProgressDialog  progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop);


        // UI init
        toolbar = findViewById(R.id.shopToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        pageTitle = findViewById(R.id.userShopTV);
//        helloUserTv = findViewById(R.id.userNameTV);
//        logoutUser = findViewById(R.id.logoutIV);
//        viewCart = findViewById(R.id.cartIV);
        searchProducts = findViewById(R.id.searchBoxET);
        categorySelected = findViewById(R.id.filteredProductTV);
//        filterProducts = findViewById(R.id.flterProdIV);
        shopRV = findViewById(R.id.shopProdListRV);

        // fireBase init
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        shopRV = findViewById(R.id.shopProdListRV);
        shopRV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        shopRV.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Store..");
        progressDialog.setCanceledOnTouchOutside(false);

        loadStore();

//        logoutUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String uid =  FirebaseAuth.getInstance().getUid();
//                Toast.makeText(getApplicationContext(), ""+uid, Toast.LENGTH_SHORT).show();
//                FirebaseAuth.getInstance().signOut();
//
//                startActivity(new Intent(getApplicationContext(),Login.class));
//                finish();
//
//            }
//        });

        // filter product
//        filterProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(UserShop.this);
//                builder.setTitle("Display By Category")
//                        .setItems(Categories.productCategoriesFilter, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String selected = Categories.productCategoriesFilter[i];
//                                if(selected.equals("All")){
////                                    loadStore();
////                                    categorySelected.setText(selected);
//                                    Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);
//
//                                    FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
//                                            .setQuery(query,ProdModel.class)
//                                            .build();
//                                    shopAdapter.updateOptions(options);
//                                }
//                                else{
//                                    // load filterd data
//                                    filterShopByCategory(selected);
//                                }
//                            }
//                        }).show();
//            }
//        });

        // search product
        searchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Query query;

                if(editable.toString().isEmpty()){

                    query = prodReff.orderBy("name", Query.Direction.ASCENDING);
                }
                else{

                    query = prodReff.whereEqualTo("name",editable.toString())
                            .orderBy("quantity",Query.Direction.ASCENDING);

                }
                FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                        .setQuery(query,ProdModel.class)
                        .build();
                shopAdapter.updateOptions(options);


            }
        });

    }

    private void filterShopByCategory(String selected) {

        categorySelected.setText(selected);
        Query query;
        query = prodReff.whereEqualTo("category", selected)
                .orderBy("name",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();
        shopAdapter.updateOptions(options);
//        db.collection("Products")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        // called when data is retrieved
//                        prodList.clear();
//                        // show data
//                        for(DocumentSnapshot snapshot: task.getResult()){
//                            String prodCategory = (String) snapshot.get(KEY_CATEGORY);
//
//                            if(selected.equals(prodCategory)){
//                                ProdModel prod = new ProdModel(snapshot.getString(KEY_ID),
//                                        snapshot.getString(KEY_NAME),
//                                        snapshot.getString(KEY_PRICE),
//                                        snapshot.getString(KEY_DESK),
//                                        snapshot.getString(KEY_CATEGORY),
//                                        snapshot.getString(KEY_QUANTITY),
//                                        snapshot.getString(KEY_EXPIRY),
//                                        snapshot.getString(KEY_IMAGEURI));
//
//                                prodList.add(prod);
//                            }
//                        }
//                        // adapter
//                        usAdapter = new UserShopAdapter(UserShop.this,prodList);
//                        // set adapter to recyclerview
//                        shopRV.setAdapter(usAdapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(UserShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void loadStore() {

        Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();

        shopAdapter = new ClientShopAdapter(options, this);

        RecyclerView shopRV = findViewById(R.id.shopProdListRV);
        // set recyclerview properties
        shopRV.setHasFixedSize(true);
        shopRV.setLayoutManager(new LinearLayoutManager(this));
        shopRV.setAdapter(shopAdapter);

    }

//    private void loadStore() {
//        prodList = new ArrayList<>();
//        progressDialog.show();
//        db.collection("Products")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        // called when data is retrieved
//                        prodList.clear();
//                        progressDialog.dismiss();
//                        // show data
//                        for(DocumentSnapshot snapshot: task.getResult()){
//                            ProdModel prod = new ProdModel(snapshot.getString(KEY_ID),
//                                                 snapshot.getString(KEY_NAME),
//                                                 snapshot.getString(KEY_PRICE),
//                                                 snapshot.getString(KEY_DESK),
//                                                 snapshot.getString(KEY_IMAGEURI),
//                                                 snapshot.getString(KEY_CATEGORY),
//                                                 snapshot.getString(KEY_QUANTITY),
//                                                 snapshot.getString(KEY_EXPIRY));
//                            prodList.add(prod);
//                        }
//                        // adapter
//                        usAdapter = new UserShopAdapter(UserShop.this,prodList);
//                        // set adapter to recyclerview
//                        shopRV.setAdapter(usAdapter);
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(UserShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    @Override
    protected void onStart() {
        super.onStart();

        shopAdapter.startListening();

//        FirebaseUser user = mAuth.getCurrentUser();
//
//        String currentId = user.getUid();
//
//        DocumentReference  reference;
//        db  = FirebaseFirestore.getInstance();
//        reference = db.collection("Users").document(currentId);
//
//        reference.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        if(task.getResult().exists()) {
//
//                            String userName = task.getResult().getString("fullName");
//                            helloUserTv.setText("Wellcome "+userName);
//
//                        }
//                        else{
//                            startActivity(new Intent(getApplicationContext(),Login.class));
//                        }
//                    }
//                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        shopAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_client,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartTb) {
            // go to user cart
            startActivity(new Intent(UserShop.this, UserCart.class));
        }
        else if (id == R.id.categoryTb) {
            // display products by category
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(UserShop.this);
            builder.setTitle("Display By Category")
                    .setItems(Categories.productCategoriesFilter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String selected = Categories.productCategoriesFilter[i];
                            if(selected.equals("All")){

                                categorySelected.setText(selected);
                                Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

                                FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                                        .setQuery(query,ProdModel.class)
                                        .build();
                                shopAdapter.updateOptions(options);
                            }
                            else{
                                // load filterd data
                                filterShopByCategory(selected);
                            }
                        }
                    }).show();

        }
        else if (id == R.id.logoutTb) {
            // logout current user

            AlertDialog.Builder builder = new AlertDialog.Builder(UserShop.this);
            builder.setTitle("Sign out");
            // Add the buttons
            builder.setMessage("Would you like to leave ?")
                    .setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // User clicked OK button
                    mAuth.signOut();
                    startActivity(new Intent(UserShop.this, Login.class));
                    finish();
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
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
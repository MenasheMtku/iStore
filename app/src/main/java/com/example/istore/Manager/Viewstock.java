package com.example.istore.Manager;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Adapter.CustomAdapter;
import com.example.istore.Adapter.ViewStockAdapter;
import com.example.istore.Login;
import com.example.istore.Model.Categories;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Viewstock extends AppCompatActivity {

    // Keys
      private static final String  KEY_ID         = "id";
//    private static final String  KEY_NAME       = "prodName";
//    private static final String  KEY_DESC       = "Description";
//    private static final String  KEY_CATEGORY   = "Category";
//    private static final String  KEY_PRICE      = "Price";
//    private static final String  KEY_EXPIRY     = "LastDate";
//    private static final String  KEY_QUANTITY   = "Quantity";
//    private static final String  KEY_IMAGEURI   = "ImageUrl";

    Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    // db declaration
    FirebaseAuth vAuth;
    // db declaration
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference prodReff = db.collection("Products");

    private  CustomAdapter adapter;
    private ViewStockAdapter stockAdapter;

    EditText searcEditText;
    TextView filteredItemName;
//    ImageView filterImageView;
    List<ProdModel> prodList = new ArrayList<>();

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstock);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        db = FirebaseFirestore.getInstance();
        vAuth = FirebaseAuth.getInstance();

        // initialize views
        searcEditText = (EditText)findViewById(R.id.searchBoxID);
//        filterImageView = (ImageView)findViewById(R.id.flterImageViewID);
        filteredItemName = (TextView) findViewById(R.id.filteredItemTV);
        mRecyclerView = findViewById(R.id.recycler_view_id);
        // set recyclerview properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
//        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
//        adapter.setHasStableIds(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Products..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // display recyclerView
//        showProducts();

        setUpRecyclerView();


        filteredItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStockByCategory();
            }
        });
        searcEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
//                    adapter.getFilter().filter(editable);
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
                stockAdapter.updateOptions(options);
            }
        });
        searcEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        filterImageView.setOnClickListener(view -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(Viewstock.this);
//            builder.setTitle("Display By Category")
//                    .setItems(Categories.productCategoriesFilter, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String selected = Categories.productCategoriesFilter[i];
//                            if(selected.equals("All")){
////                                showProducts();
//                                filteredItemName.setText("All Products");
//                               Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);
//
//                                FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
//                                        .setQuery(query,ProdModel.class)
//                                        .build();
//                                stockAdapter.updateOptions(options);
////                                setUpRecyclerView();
//                            }
//                            else{
//                                // load filterd data
//                                filterByCategory(selected);
//                            }
//                        }
//                    }).show();
//        });


    }

    private void displayStockByCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Viewstock.this);
            builder.setTitle("Display By Category")
                    .setItems(Categories.productCategoriesFilter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String selected = Categories.productCategoriesFilter[i];
                            if (selected.equals("All")) {

                                filteredItemName.setText("All Products");
                                Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

                                FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                                        .setQuery(query, ProdModel.class)
                                        .build();
                                stockAdapter.updateOptions(options);
                            } else {
                                // load filterd data
                                filterShopByCategory(selected);
                            }
                        }
                    }).show();
    }

    private void setUpRecyclerView() {

        Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();

        stockAdapter = new ViewStockAdapter(options, this);



        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                RecyclerView mRecyclerView = findViewById(R.id.recycler_view_id);
                // set recyclerview properties
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(Viewstock.this));
                mRecyclerView.setAdapter(stockAdapter);
                progressDialog.dismiss();


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        stockAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stockAdapter.stopListening();
    }

//    private void filterByCategory(String selected) {
//
//
//        Query query;
//
//        query = prodReff.whereEqualTo("category", selected)
//                .orderBy("name",Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
//                .setQuery(query,ProdModel.class)
//                .build();
//        stockAdapter.updateOptions(options);
//
//
//    }

     public void showProducts() {

        // set title of progressDialog
//        pd.setTitle("Loading Data...");
        // show progressDialog
        progressDialog.show();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // called when data is retrieved
                        prodList.clear();
                        progressDialog.dismiss();
                        // show data
                        for(DocumentSnapshot snapshot: task.getResult()){
                            ProdModel prod = new ProdModel(snapshot.getString(KEY_ID),
                                    snapshot.getString("name"),
                                    snapshot.getString("price"),
                                    snapshot.getString("description"),
                                    snapshot.getString("quantity"),
                                    snapshot.getString("expiry"),
                                    snapshot.getString("imageUrl"),
                                    snapshot.getString("category"));
//                                                snapshot.getString(KEY_NAME),
//                                                snapshot.getString(KEY_PRICE),
//                                                snapshot.getString(KEY_DESC),
//                                                snapshot.getString(KEY_CATEGORY),
//                                                snapshot.getString(KEY_QUANTITY),
//                                                snapshot.getString(KEY_EXPIRY),
//                                                snapshot.getString(KEY_IMAGEURI));

                            Log.i("TAG", "onComplete: \n"+prod.toString());
                            prodList.add(prod);

                        }
                        // adapter
                        adapter = new CustomAdapter(Viewstock.this,prodList);
                        // set adapter to recyclerview
                        mRecyclerView.setAdapter(adapter);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Viewstock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }


    private void filterShopByCategory(String selected) {


        filteredItemName.setText(selected);
        Query query;
        query = prodReff.whereEqualTo("category", selected)
                .orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query, ProdModel.class)
                .build();
        stockAdapter.updateOptions(options);
    }


    // display items by category
    //        db.collection("Products").orderBy("name")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        // called when data is retrieved
//                        prodList.clear();
//                        // show data
//                        for(DocumentSnapshot snapshot: task.getResult()){
//                            String prodCategory = (String) snapshot.get("category");
//
//                            if(selected.equals(prodCategory)){
//                                ProdModel prod = new ProdModel(snapshot.getString(KEY_ID),
//                                                     snapshot.getString("name"),
//                                                     snapshot.getString("price"),
//                                                     snapshot.getString("description"),
//                                                     snapshot.getString("quantity"),
//                                                     snapshot.getString("expiry"),
//                                                     snapshot.getString("imageUrl"),
//                                                     snapshot.getString("category"));
//                                prodList.add(prod);
//                            }
//                        }
//                        // adapter
//                        adapter = new CustomAdapter(Viewstock.this,prodList);
//                        // set adapter to recyclerview
//                        mRecyclerView.setAdapter(adapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(Viewstock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    // Delete Items From Stock
//    public void deleteData(int index){
//        // set title of progressDialog
//        pd.setTitle("Deleting Data...");
//        // show progressDialog
//        pd.show();
//
//        db.collection("Products").document(prodList.get(index).getId())
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        pd.dismiss();
//                        Toast.makeText(Viewstock.this, "Item Deleted", Toast.LENGTH_SHORT).show();
//                        showProducts();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(Viewstock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}

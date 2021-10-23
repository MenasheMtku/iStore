package com.example.istore.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.istore.Adapter.Admin.ViewStockAdapter;
import com.example.istore.Model.Categories;
import com.example.istore.Model.ProdModel;
import com.example.istore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StorageManager extends AppCompatActivity {

    Toolbar toolbar;
    EditText searchEditText;
    TextView filteredItemName;
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    List<ProdModel> prodList = new ArrayList<>();
    private ViewStockAdapter stockAdapter;

    // db declaration
    FirebaseAuth vAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference prodReff = db.collection("Products");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manager);

        toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        vAuth = FirebaseAuth.getInstance();

        // initialize views
        searchEditText = (EditText)findViewById(R.id.searchBoxID);
        filteredItemName = (TextView) findViewById(R.id.filteredItemTV);

        mRecyclerView = findViewById(R.id.mangerViewRv);
        // set recyclerview properties
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(StorageManager.this, 2));

        // set recyclerview properties
//        mRecyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Products..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        displayStock();


        filteredItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStockByCategory();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable text) {

                Query query;

                if(text.toString().isEmpty()){

                    query = prodReff.orderBy("name");
                }
                else{

                    query = prodReff.whereEqualTo("name",text.toString())
                            .orderBy("name",Query.Direction.ASCENDING);

                }
                FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions
                        .Builder<ProdModel>()
                        .setQuery(query,ProdModel.class)
                        .build();
                stockAdapter.updateOptions(options);
            }
        });
    }

    private void displayStock() {

        Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions
                .Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();
        stockAdapter = new ViewStockAdapter(options, this);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                mRecyclerView.setAdapter(stockAdapter);
                progressDialog.dismiss();


            }
        });

    }

    private void displayStockByCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(StorageManager.this);
        builder.setTitle("Display By Category")
                .setItems(Categories.productCategoriesFilter, new DialogInterface.OnClickListener() {
                    Query query ;
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String selected = Categories.productCategoriesFilter[i];

                        if (selected.equals("All")) {

                            filteredItemName.setText("All Products");
                            query = prodReff.orderBy("name", Query.Direction.ASCENDING);

                            FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions
                                    .Builder<ProdModel>()
                                    .setQuery(query, ProdModel.class)
                                    .build();
                            stockAdapter.updateOptions(options);
                        }
                        else {
                            filteredItemName.setText(selected);
                            query = prodReff
                                    .whereEqualTo("category", selected)
                                    .orderBy("name", Query.Direction.ASCENDING);

                            FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions
                                    .Builder<ProdModel>()
                                    .setQuery(query, ProdModel.class)
                                    .build();
                            stockAdapter.updateOptions(options);
                        }
                    }
                }).show();
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
}
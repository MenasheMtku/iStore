package com.example.istore.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.istore.Adapter.Emp.ViewStorageEmpAdapter;
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

public class ViewStorage extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    EditText searchEditText;

    private ViewStorageEmpAdapter empItemsAdapter;

    FirebaseAuth eAuth;
    // db declaration
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference prodReff = db.collection("Products");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_storage);


        toolbar = findViewById(R.id.serchStorageTb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        eAuth = FirebaseAuth.getInstance();

        mRecyclerView = findViewById(R.id.rcvEmpId);
        searchEditText = findViewById(R.id.searchEmpBoxID);

        setUpRecyclerView();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {

                loadSearchQuery(s);
            }
        });

        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

    }

    private void loadSearchQuery(Editable s) {

        Query query;

        if(s.toString().isEmpty()){

            query = prodReff.orderBy("name", Query.Direction.ASCENDING);
        }
        else{

            query = prodReff.whereEqualTo("name",s.toString())
                    .orderBy("quantity",Query.Direction.ASCENDING);

        }
        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();
        empItemsAdapter.updateOptions(options);
    }

    private void setUpRecyclerView() {

        Query query = prodReff.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProdModel> options = new FirestoreRecyclerOptions.Builder<ProdModel>()
                .setQuery(query,ProdModel.class)
                .build();

        empItemsAdapter = new ViewStorageEmpAdapter(options, this);



        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                 mRecyclerView = findViewById(R.id.rcvEmpId);
                // set recyclerview properties
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewStorage.this));
                mRecyclerView.setAdapter(empItemsAdapter);
//                progressDialog.dismiss();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        empItemsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        empItemsAdapter.stopListening();
    }
}
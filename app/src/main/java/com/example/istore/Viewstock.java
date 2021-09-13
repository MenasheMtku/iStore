package com.example.istore;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Adapter.CustomAdapter;
import com.example.istore.Model.Prod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Viewstock extends AppCompatActivity {



    RecyclerView mRecyclerView;
    // layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    // firestore instance
    FirebaseFirestore db;

    CustomAdapter adapter;
    ProgressDialog pd;
    EditText searcEditText;
    List<Prod> prodList = new ArrayList<>();
//    FloatingActionButton floatingActionButton;

    // Keys
    private static final String  KEY_ID = "id";
    private static final String  KEY_NAME = "prodtName";
    private static final String  KEY_CATEGORY = "prodCategory";
    private static final String  KEY_EXPIRY = "prodExpirationDate";
    private static final String  KEY_QUANTITY = "prodQuantity";
    private static final String  KEY_IMAGEURI = "prodImageUrl";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstock);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        // initialize views
        searcEditText = (EditText)findViewById(R.id.searchBoxID);
        mRecyclerView = findViewById(R.id.recycler_view_id);
        // set recyclerview properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
//        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        pd = new ProgressDialog(this);

        // display recyclerView
        showData();



//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Viewstock.this, AddProduct.class);
//                startActivity(intent);
//            }
//        });
        searcEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

//            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable editable) {

                    adapter.getFilter().filter(editable);
            }
        });
        searcEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);


    }

    private void showData() {

        // set title of progressDialog
        pd.setTitle("Loading Data...");
        // show progressDialog
        pd.show();
        db.collection("Products").orderBy(KEY_QUANTITY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // called when data is retrieved
                        prodList.clear();
                        pd.dismiss();
                        // show data
                        for(DocumentSnapshot snapshot: task.getResult()){
                            Prod prod = new Prod(snapshot.getString(KEY_ID),
                                                snapshot.getString(KEY_NAME),
                                                snapshot.getString(KEY_QUANTITY),
                                                snapshot.getString(KEY_EXPIRY),
                                                snapshot.getString(KEY_IMAGEURI),
                                                snapshot.getString(KEY_CATEGORY));
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
                        pd.dismiss();
                        Toast.makeText(Viewstock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
    // Delete Items From Stock
    public void deleteData(int index){
        // set title of progressDialog
        pd.setTitle("Deleting Data...");
        // show progressDialog
        pd.show();

        db.collection("Products").document(prodList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Viewstock.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        showData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Viewstock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return true;

    }

}

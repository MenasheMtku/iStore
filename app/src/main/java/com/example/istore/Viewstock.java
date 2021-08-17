package com.example.istore;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istore.Model.CustomAdapter;
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

    List<Prod> prodList = new ArrayList<>();

    RecyclerView mRecyclerView;
    // layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    // firestore instance
    FirebaseFirestore db;

    CustomAdapter adapter;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstock);

        Toolbar toolbar = findViewById(R.id.toolbar_m_id);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        // initialize views
        mRecyclerView = findViewById(R.id.recycler_view_id);
        // set recyclerview properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);

        // display recyclerView
        showData();
    }

    private void showData() {

        // set title of progressDialog
        pd.setTitle("Loading Data...");
        // show progressDialog
        pd.show();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // called when data is retrieved
                        prodList.clear();
                        pd.dismiss();
                        // show data
                        for(DocumentSnapshot snapshot: task.getResult()){
                            Prod prod = new Prod(snapshot.getString("id"),
                                    snapshot.getString("name"),
                                    snapshot.getString("qty"),
                                    snapshot.getString("expiry"));
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

    // Search Specific Item METHOD
    private void searchData(String s) {
        pd.setTitle("Searching...");
        pd.show();

        db.collection("Products").whereEqualTo("search", s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // called when searching  is succeed
                        prodList.clear();
                        pd.dismiss();
                        // show data
                        for(DocumentSnapshot snapshot: task.getResult()){
                            Prod prod = new Prod(snapshot.getString("id"),
                                    snapshot.getString("name"),
                                    snapshot.getString("qty"),
                                    snapshot.getString("expiry"));
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
        pd.setTitle("Loading Data...");
        // show progressDialog
        pd.show();

        db.collection("Products").document(prodList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Viewstock.this, "Deleted", Toast.LENGTH_SHORT).show();
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

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
}

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
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private CollectionReference prodReference = db.collection("Products");
//
//    private ItemAdapter adapter;
//
//    private AlertDialog.Builder builder;
//    private AlertDialog dialog;
//    private LayoutInflater inflater;
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
//    private void seUpRecyclerView() {
//
//        // item_m = new Item_m();
//        Query query = prodReference.orderBy("quantity", Query.Direction.DESCENDING);
//
//        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
//                .setQuery(query, Item.class)
//                .build();
//
//        adapter = new ItemAdapter(options);
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
//
//                Item item = documentSnapshot.toObject(Item.class);
//                String id = documentSnapshot.getId();
//                String name1 = String.valueOf(documentSnapshot.get("name"));
//                String qyt = String.valueOf(documentSnapshot.get("quantity"));
//                String date = String.valueOf(documentSnapshot.get("expiry"));
//
//                Toast.makeText(Viewstock.this,
//                        "Position: " + position
//                                + "\nID: " + id
//                                + "\nName: " + name1
//                                + "\nQyt: " + qyt
//                                + "\nDate: " + date
//                        , Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//        adapter.setOnItemLongClickListener(new ItemAdapter.OnItemLongClickListener() {
//            @Override
//            public void OnItemLongClick(final View view, final int position) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(Viewstock.this);
//                builder.setTitle("Options");
//                String[] options = {"Delete", "Update"};
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (which == 0) {
//                            //Delete is clicked
//                            deleteDialog(position);
//                        }
//                        if (which == 1) {
//                            //Update is clicked
//                            updateDialog(view, position);
//
//                        }
//                    }
//                }).create().show();
//            }
//        });
//
//    }
//
//    private void updateDialog(View view, int position) {
//
//        builder = new AlertDialog.Builder(Viewstock.this);
//        inflater = LayoutInflater.from(Viewstock.this);
//        view = inflater.inflate(R.layout.update_popup, null);
//
//        TextView popupTitle = view.findViewById(R.id.title_popup_id);
//        // Items info
//        final EditText itemName = view.findViewById(R.id.itemName_popup_id);
//        final EditText itemQty = view.findViewById(R.id.itemQyt_popup_id);
//        final EditText itemExpiry = view.findViewById(R.id.itemExpiry_popup_id);
//        // Button
//        Button updateButton = view.findViewById(R.id.update_btn_popup_id);
//
//        final EditText[] editTextsArr = {itemName, itemQty, itemExpiry};
//
//        db.collection("Products")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Log.d("Document id: ", document.getId() + " => " + document.getData());
//                                Item item = document.toObject(Item.class);
//                                String popName = String.valueOf(document.get("name"));
//                                String popQyt = String.valueOf(document.get("quantity"));
//                                String popExp = String.valueOf(document.get("expiry"));
//
//                                itemName.setText(popName);
//                                itemQty.setText(popQyt);
//                                itemExpiry.setText(popExp);
//
//
//                            }
//                        } else {
//                            Log.d("Error", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        for (EditText ed : editTextsArr) {
//            ed.setText("");
//        }
//        builder.setView(view);
//        dialog = builder.create();
//        dialog.show();
//
//    }

//    private void deleteDialog(final int position) {
//
//        builder = new AlertDialog.Builder(Viewstock.this);
//        inflater = LayoutInflater.from(Viewstock.this);
//        final View view = inflater.inflate(R.layout.confirmation_pop, null);
//
//        Button noButton = view.findViewById(R.id.conf_no_btn);
//        Button yesButton = view.findViewById(R.id.conf_yes_btn);
//
//        builder.setView(view);
//        dialog = builder.create();
//        dialog.show();
//
//        yesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.deleteItem(position);
//                dialog.dismiss();
//            }
//        });
//        noButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


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

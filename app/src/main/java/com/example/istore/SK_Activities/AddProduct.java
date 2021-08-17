package com.example.istore.SK_Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.istore.Model.Prod;
import com.example.istore.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    private EditText itemName, itemQuantity, itemExpriedDate;
    Button saveItem, clearFields;

    // Fireestore instance
    FirebaseFirestore db;
    CollectionReference dbReference;
    // Progress Dialog
    ProgressDialog pd;

    Calendar cal;
    DatePickerDialog datePicker;
    String pId, pName, pQuantity,pExpiry;

    Prod prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//        ActionBar actionBar = getSupportActionBar();


        //firestore
        db = FirebaseFirestore.getInstance();
        dbReference = db.collection("Products");
        //model
        prod = new Prod();

        itemName = (EditText) findViewById(R.id.etName);
        itemQuantity = (EditText) findViewById(R.id.etQuantity);
        itemExpriedDate = (EditText) findViewById(R.id.etExpiredDate);

        saveItem = (Button) findViewById(R.id.saveBtn);
        clearFields = (Button) findViewById(R.id.clearBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //update data
//            actionBar.setTitle("Update Product");
            saveItem.setText("Update Data");
            saveItem.setTextColor(Color.BLUE);
            // get data
            pId = bundle.getString("pId");
            pName = bundle.getString("pName");
            pQuantity = bundle.getString("pQuantity");
            pExpiry = bundle.getString("pExpiry");

            itemName.setText(pName);
            itemQuantity.setText(pQuantity);
            itemExpriedDate.setText(pExpiry);
            Log.i("pID:", pId);
            //Log.i("pSearch:", pSearch);
        }
        else{
//            actionBar.setTitle("Add Product");
            // new data
            saveItem.setText("S a v e");
            // saveItem.setTextColor(Color.GREEN);

        }
        pd = new ProgressDialog(this);

        //choose date
        itemExpriedDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(AddProduct.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        itemExpriedDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);
                datePicker.show();
            }
        });

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1  = getIntent().getExtras();
                if (bundle1 != null){
                    //updating
                    String id = pId;
                    String name = itemName.getText().toString().trim();
                    String quntity  = itemQuantity.getText().toString().trim();
                    String expiry  = itemExpriedDate.getText().toString().trim();

                    //  funct call update data
                    updateData(id, name,quntity,expiry);

                }
                else{
                    // Add New
                    // input data
                    String name = itemName.getText().toString().trim();
                    String qty  = itemQuantity.getText().toString().trim();
                    String expiry  = itemExpriedDate.getText().toString().trim();

                    if(name.isEmpty() || qty.isEmpty() || expiry.isEmpty()){
                        Toast.makeText(AddProduct.this, "Mandatory Fields Are Empty ", Toast.LENGTH_SHORT).show();

                    }else {
                        // func called to upload date 2 firebase
                        uploadData(name, qty, expiry);
                    }
                }
            }
        });
        clearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemName.setText("");
                itemName.setFocusable(true);
                itemQuantity.setText("");
                itemExpriedDate.setText("");
            }
        });
    }

    private void updateData(String id, String name, String quantity, String expiry) {
        pd.setTitle("Updating Data...");
        pd.show();

        db.collection("Products").document(id)
                .update("name", name, "expiry",expiry,"qty",quantity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       pd.dismiss();
                        Toast.makeText(AddProduct.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { 
                        pd.dismiss();
                        Toast.makeText(AddProduct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadData(String name, String qty, String expiry) {
        pd.setTitle("Adding To Firebase");
        pd.show();
        // random id to each data to be stored
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id",id);
        doc.put("name", name);
        doc.put("qty", qty);
        doc.put("expiry", expiry);
        doc.put("search", name.toLowerCase());
        // Add this data
        db.collection("Products")
                .document(id)
                .set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        itemName.setText("");
                        itemName.setFocusable(true);
                        itemQuantity.setText("");
                        itemExpriedDate.setText("");
                        Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setupFloatingLabelError() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.pName_text_input_layout);
        Objects.requireNonNull(floatingUsernameLabel.getEditText()).addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 0 && text.length() <= 4) {
                    floatingUsernameLabel.setError(getString(R.string.item_name));
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

package com.example.istore.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.istore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddress extends AppCompatActivity {

    Toolbar toolbar;
    TextView title;
    Button addAddressBtn;
    EditText name, address, city, postalCode, phone;

    FirebaseAuth uAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        uAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title = (TextView) findViewById(R.id.textView8);
        name = (EditText) findViewById(R.id.ad_name);
        address = (EditText) findViewById(R.id.ad_address);
        city = (EditText) findViewById(R.id.ad_city);
        postalCode = (EditText) findViewById(R.id.ad_code);
        phone = (EditText) findViewById(R.id.ad_phone);
        addAddressBtn = (Button) findViewById(R.id.ad_add_address);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName       = name.getText().toString();
                String userAddress    = address.getText().toString();
                String userCity       = city.getText().toString();
                String userPostalCode = postalCode.getText().toString();
                String userPhone      = phone.getText().toString();

                String final_address = "";
                String final_name = "";
                String final_phone = "";
                String final_postal_code = "";

                if(!userName.isEmpty()){
                    final_name += userName;
                }
                if(!userPhone.isEmpty()){
                    final_phone += userPhone;
                }
                if(!userPostalCode.isEmpty()){
                    final_postal_code += userPostalCode;
                }
                if(!userAddress.isEmpty()){
                    final_address += userAddress;
                }
                if(!userCity.isEmpty() ){
                    final_address = final_address+" , "+ userCity;
                }
                        if(!userName.isEmpty()
                        && !userAddress.isEmpty()
                        && !userCity.isEmpty()
                        && !userPostalCode.isEmpty()
                        && !userPhone.isEmpty()){

                    Map<String, String> addMap = new HashMap<>();

                    addMap.put("address", final_address);
                    addMap.put("name", final_name);
                    addMap.put("postalCode", final_postal_code);
                    addMap.put("phone", final_phone);

                    firestore.collection("Users")
                             .document(uAuth.getCurrentUser().getUid())
                             .collection("Address")
                             .add(addMap)
                             .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    if (task.isSuccessful()){

                                        clearField();

                                        Toast.makeText(AddAddress.this,
                                                "Created Successfully.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                        else {
                            Toast.makeText(AddAddress.this,
                                    "Fill all fields", Toast.LENGTH_SHORT).show();
                        }
            }
        });




    }

    private void clearField() {

        name.setText("");
        address.setText("");
        city.setText("");
        postalCode.setText("");
        phone.setText("");
    }
}
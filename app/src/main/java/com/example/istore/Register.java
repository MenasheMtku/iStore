package com.example.istore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI init
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);

        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        // firestore and auth init
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();



        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if(valid){
                    mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_LONG).show();

                                    DocumentReference df = mFirestore.collection("Users").document(user.getUid());
//                                    usersCollecRef.document(user.getUid());
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("fullName", fullName.getText().toString().trim());
                                    userInfo.put("password", password.getText().toString().trim());
                                    userInfo.put("emailAddress", email.getText().toString().trim());
                                    userInfo.put("phoneNumber", phone.getText().toString().trim());
                                    userInfo.put("isUser", "1");

                                    df.set(userInfo);

                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.i("Auth", "Error Message: \n"+e.getMessage());

                                }
                            });
                }
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
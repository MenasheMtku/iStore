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

import com.example.istore.Employee.EmployeeDashboard;
import com.example.istore.Manager.ManagerDashboard;
import com.example.istore.User.UserShop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

//    private static final String TAG = "Login";
//    int AUTHUI_REQUEST_CODE = 1111;

    EditText email,password;
    Button loginBtn,gotoRegister;
    boolean valid = true;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // firestore and auth init
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        // UI init
        password = findViewById(R.id.loginPassword);
        email = findViewById(R.id.loginEmail);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkField(email);
                checkField(password);

                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if(valid){
                    mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            Toast.makeText(Login.this,
                                    "Logged In successfully!", Toast.LENGTH_SHORT).show();

                            checkUserAccessLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            }
        });

    }
    private void checkUserAccessLevel(String uid) {

        DocumentReference df  = mFirestore.collection("Users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("Doc Info", "onSuccess: "+ documentSnapshot.getData());

                if(documentSnapshot.getString("isAdmin") != null){
                    startActivity(new Intent(getApplicationContext(), ManagerDashboard.class));
                    finish();
                }
                if(documentSnapshot.getString("isEmp") != null){
                    startActivity(new Intent(getApplicationContext(), EmployeeDashboard.class));
                    finish();
                }
                if(documentSnapshot.getString("isUser") != null){
                    startActivity(new Intent(getApplicationContext(), UserShop.class));
                    finish();
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

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));

            startActivity(new Intent(getApplicationContext(),UserShop.class));
            finish();
        }
    }
}

//    public void handleLoginRegister(View view){
//
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build());
//
//        Intent intent = AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .setLogo(R.drawable.choices)
//                .setTheme(R.style.AppTheme)
//                .setAlwaysShowSignInMethodScreen(true)
//                .build();
//
//        startActivityForResult(intent,AUTHUI_REQUEST_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == AUTHUI_REQUEST_CODE){
//            if(resultCode == RESULT_OK){
//                // user logged in or new user has registered
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(TAG, "onActivityResult: "+user.getEmail());
//                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
//                    Toast.makeText(this, "Welcome new user" , Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(this, "Welcome back" , Toast.LENGTH_SHORT).show();
//                }
//
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
//                this.finish();
//            }
//            else{
//                // Signing in failed
//                IdpResponse response = IdpResponse.fromResultIntent(data);
//                if(response == null){
//                    Log.d(TAG, "onActivityResult: the user has canceled the sign in request");
//                }
//                else {
//                    Log.e(TAG, "onActivityResult: ",response.getError() );
//                }
//            }
//        }
//    }
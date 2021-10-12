package com.example.istore;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

//    EditText etName, etPass;
//    EditText et_email, et_password;
    TextView dashboardTv;
    Button logoutButton;
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboardTv = (TextView) findViewById(R.id.dashboardID);
        logoutButton = (Button) findViewById(R.id.logoutBtn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
//        mAuth = FirebaseAuth.getInstance();
//        etName = (EditText) findViewById(R.id.etName);
//        etPass = (EditText) findViewById(R.id.etPass);
//        et_email = (EditText) findViewById(R.id.emailet);
//        et_password = (EditText) findViewById(R.id.passwordet);
//        loginButton = (Button) findViewById(R.id.loginBtn);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = et_email.getText().toString().trim();
//                String password = et_password.getText().toString().trim();
//
//                if(TextUtils.isEmpty(email)){
//
//                    Toast.makeText(MainActivity.this,"Email is required" , Toast.LENGTH_SHORT).show();
//
//                }
//                if(TextUtils.isEmpty(password)){
//
//                    Toast.makeText(MainActivity.this,"Password is required" , Toast.LENGTH_SHORT).show();
//
//                }
//                if(password.length()< 6 ){
//
//                    Toast.makeText(MainActivity.this,"Password is too short, 6 and above." , Toast.LENGTH_SHORT).show();
//
//                }
//
//            mAuth.signInWithEmailAndPassword(email,password)
//                    .addOnCompleteListener(MainActivity.this, task -> {
//
//                        if(task.isSuccessful()){
//
//                            startActivity(new Intent(getApplicationContext(), ManagerDashboard.class));
//                        }
//                        else{
//                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//            }
//        });

    }



//    public void check(View view) {
//        //if (etName.getText().toString().equals("admin") && etPass.getText().toString().equals("123")){
//        if (etName.getText().toString().equals("m") && etPass.getText().toString().equals("1")) {
//            etName.setText("");
//            etPass.setText("");
//
//            Intent p = new Intent(this, ManagerDashboard.class);
//            startActivity(p);
//        }
//        //else if(etName.getText().toString().equals("worker") && etPass.getText().toString().equals("222")){
//        else if (etName.getText().toString().equals("w") && etPass.getText().toString().equals("2")) {
//            etName.setText("");
//            etPass.setText("");
//
//            Intent p = new Intent(this, EmployeeDashboard.class);
//            startActivity(p);
//
//        } else {
//
//            etName.setText("");
//            etPass.setText("");
//
//            showMessage("Error!!", "Access Denied Trying to Connect to Administrative!");
//        }
//
//    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

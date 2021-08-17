package com.example.istore;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etName = (EditText) findViewById(R.id.etName);
        etPass = (EditText) findViewById(R.id.etPass);
    }

    public void check(View view) {
        //if (etName.getText().toString().equals("admin") && etPass.getText().toString().equals("123")){
        if (etName.getText().toString().equals("m") && etPass.getText().toString().equals("1")) {
            etName.setText("");
            etPass.setText("");

            Intent p = new Intent(this, Manager.class);
            startActivity(p);
        }
        //else if(etName.getText().toString().equals("worker") && etPass.getText().toString().equals("222")){
        else if (etName.getText().toString().equals("w") && etPass.getText().toString().equals("2")) {
            etName.setText("");
            etPass.setText("");

            Intent p = new Intent(this, Storekeeper.class);
            startActivity(p);

        } else {

            etName.setText("");
            etPass.setText("");

            showMessage("Error!!", "Access Denied Trying to Connect to Administrative!");
        }

    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

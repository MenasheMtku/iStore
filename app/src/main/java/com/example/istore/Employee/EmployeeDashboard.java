package com.example.istore.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.istore.Login;
import com.example.istore.R;
import com.google.firebase.auth.FirebaseAuth;

public class EmployeeDashboard extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth mAuth;
    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        toolbar = (Toolbar) findViewById(R.id.storeKeeperTb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        mainGrid = (GridLayout) findViewById(R.id.mainGraid2_id);
        setSinglEvent(mainGrid);
    }

    private void setSinglEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalI == 0)//open addNewProd activity
                    {
                        Intent p = new Intent(EmployeeDashboard.this, AddProduct.class);
                        startActivity(p);
                        Log.i("message:", "AddNewProd selected");
                    } else if (finalI == 1)
                        //open search activity
                    {
                        Intent p = new Intent(EmployeeDashboard.this, ViewStorage.class);
                        startActivity(p);
                        Log.i("message:", "ViewStoreage selected");
                    } else if (finalI == 2)
                        //open mReport activity
                    {
                        Intent p = new Intent(EmployeeDashboard.this, ExpiredProduct.class);
                        startActivity(p);
                        Log.i("message:", "addNewProd success");
                    } else if (finalI == 3)//open Ventor_return activity
                    {
//                        Intent p = new Intent(EmployeeDashboard.this, sVendorReturn.class);
//                        startActivity(p);
//                        Log.i("message:", "addNewProd success");
                    } else {
                        Toast.makeText(EmployeeDashboard.this, "Please set activity for this ITEM",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.logoutEmployee){
            // logout Employee

            showLogoutDoialog();
        }
        return true;
    }

    private void showLogoutDoialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign out")
                .setCancelable(false);
        builder.setMessage("Are you sure ?");
        builder.setPositiveButton("Yes", (dialog, id) -> {

            logoutEmployee();

        });
        builder.setNegativeButton("No", (dialog, id) -> {

            // User cancelled the dialog
            dialog.dismiss();

        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutEmployee() {

        mAuth.signOut();
        startActivity(new Intent(EmployeeDashboard.this, Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(false);

        showLogoutDoialog();
    }
}

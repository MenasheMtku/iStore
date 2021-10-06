package com.example.istore.Manager;

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

import com.example.istore.Employee.ExpiredProduct;
import com.example.istore.Employee.Storekeeper;
import com.example.istore.Employee.ViewStorage;
import com.example.istore.Login;
import com.example.istore.R;
import com.google.firebase.auth.FirebaseAuth;

public class Manager extends AppCompatActivity {

    GridLayout mainGrid;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mainGrid = (GridLayout) findViewById(R.id.mainGraid2_id);
        setSinglEvent(mainGrid);
    }

    private void setSinglEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            CardView cardView1 = (CardView) mainGrid.getChildAt(i);
            final int finalIi = i;

            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalIi == 0) {
                        Intent p = new Intent(getApplicationContext(), OrderToStock.class);
                        startActivity(p);

                    } else if (finalIi == 1) {
                        Intent p = new Intent(getApplicationContext(), Viewstock.class);
                        startActivity(p);

                    } else if (finalIi == 2) {
                        Intent p = new Intent(getApplicationContext(), CustomerOrders.class);
                        startActivity(p);

                    }
                    else if (finalIi == 3) {
                        Intent p = new Intent(getApplicationContext(), ExpiredProduct.class);
                        startActivity(p);

                    }
                    else {
                        Toast.makeText(Manager.this, "Please set activity for this ITEM",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.logoutAdmin){
            // logout Employee

            logOutDialog();


        }
        return true;
    }

    private void logOutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setTitle("Sign out")
                .setCancelable(true);
        builder.setMessage("Are you sure ?");
        builder.setPositiveButton("Yes", (dialog, id) -> {

            logoutAdmin();

        });
        builder.setNegativeButton("No", (dialog, id) -> {

            // User cancelled the dialog
            dialog.dismiss();

        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutAdmin() {

        mAuth.signOut();
        startActivity(new Intent(Manager.this, Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(false);

        logOutDialog();
    }
}

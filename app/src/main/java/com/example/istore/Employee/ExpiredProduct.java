package com.example.istore.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.istore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ExpiredProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired_product);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.buttomNavigationId);
//
//        bottomNavigationView.setSelectedItemId(R.id.viewExpired_buttom_nav);
//
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()){
//
//                    case R.id.addItem_buttom_nav:
//
//                        startActivity(new Intent(getApplicationContext(), AddProduct.class));
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.viewExpired_buttom_nav:
//                        // // current page
//                        return true;
//
//                    case R.id.viewStorage_buttom_nav:
//
//                        startActivity(new Intent(getApplicationContext(), ViewStorage.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });
    }
}
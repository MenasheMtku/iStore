package com.example.istore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.istore.SK_Activities.AddProduct;

public class Storekeeper extends AppCompatActivity {

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sk_id);
        setSupportActionBar(toolbar);

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
                        Intent p = new Intent(Storekeeper.this, AddProduct.class);
                        startActivity(p);
                        Log.i("message:", "addNewProd success");
                    } else if (finalI == 1)//open search activity
                    {
//                        Intent p = new Intent(Storekeeper.this, sSearchProd.class);
//                        startActivity(p);
//                        Log.i("message:", "SearchProd success");
                    } else if (finalI == 2)//open mReport activity
                    {
//                        Intent p = new Intent(Storekeeper.this, sMreport.class);
//                        startActivity(p);
//                        Log.i("message:", "addNewProd success");
                    } else if (finalI == 3)//open Ventor_return activity
                    {
//                        Intent p = new Intent(Storekeeper.this, sVendorReturn.class);
//                        startActivity(p);
//                        Log.i("message:", "addNewProd success");
                    } else {
                        Toast.makeText(Storekeeper.this, "Please set activity for this ITEM",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_sk, menu);
//        return true;
//    }
}

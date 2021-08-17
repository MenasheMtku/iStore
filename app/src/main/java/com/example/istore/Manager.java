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

public class Manager extends AppCompatActivity {

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                    if (finalIi == 1) {
                       Intent p = new Intent(Manager.this, Viewstock.class);
                        startActivity(p);
                        Log.i("message:", "opennin firebase...");
                    } else {
                        Toast.makeText(Manager.this, "Please set activity for this ITEM",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}

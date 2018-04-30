package com.ibm.mobile.jsonstoreofflinesync.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.ibm.mobile.jsonstoreofflinesync.manager.FlightAttendantManager;
import com.ibm.mobile.jsonstoreofflinesync.manager.FoodMenuSyncListener;



public class MainPageActivity extends AppCompatActivity implements FoodMenuSyncListener{

    ProgressBar progressBar;

    @Override
    public void foodSyncComplete(final String message) {
        runOnUiThread(new Runnable(){
                          @Override
                          public void run() {
                              progressBar.setVisibility(View.INVISIBLE);
                              Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                          }
                      }

        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate is used to inflate the custom toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Show the progress bar on click of Sync button and start Downstream Sync
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
        FlightAttendantManager.getInstance().syncFoodMenu(this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        //Setting the name of the Application
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the flight number that is passed from the intent
        TextView FlightNo = findViewById(R.id.tvFlightNo);
        String stringFNo = getIntent().getStringExtra("flightNo");
        FlightNo.setText(stringFNo);


        Button btnInFlightMenu = findViewById(R.id.btn_inflightmenu);
        btnInFlightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This is the intent for navigating from Main Page to Menu Page
                Intent intentaftersync =new Intent(getApplicationContext(),FoodMenuActivity.class);
                //Go to the FoodMenu Page after this
                startActivity(intentaftersync);
            }
        });

        Button btnMakeSale = findViewById(R.id.btn_make_sale);
        btnMakeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This intent is for Going to the Order Details Page after Main Page after clicking on "Make a sale" Button
                Intent intent =new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        Button btnShowSales = findViewById(R.id.btn_show_sales);
        btnShowSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenttoshowsales = new Intent(getApplicationContext(),ShowSalesActivity.class);
                startActivity(intenttoshowsales);
            }
        });

    }

}

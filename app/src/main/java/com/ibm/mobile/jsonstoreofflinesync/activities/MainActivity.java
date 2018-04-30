package com.ibm.mobile.jsonstoreofflinesync.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


//Start Page  - Attendant will enter the Flight Number and click on flight Icon
public class MainActivity extends AppCompatActivity{
        @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        //Name of the application
        getSupportActionBar().setTitle( "MEALS WITH WINGS");

        //This takes the edit textview containing flight number
        final EditText flightNo = findViewById(R.id.etFlightNo);

        ImageView btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On clicking the flight icon button , the intent is to navigate from this opening page to the main page of this application.
                Intent mainActivityIntent =new Intent(getApplicationContext(), MainPageActivity.class);
                //putExtra does the work of passing the Flight Number to next Page along with the intent of navigating
                mainActivityIntent.putExtra("flightNo",flightNo.getText().toString());
                //This will start the next Intent
                startActivity(mainActivityIntent);
            }
        });

        }

}
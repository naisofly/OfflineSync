package com.ibm.mobile.jsonstoreofflinesync.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.mobile.jsonstoreofflinesync.adapters.FoodMenuAdapter;
import com.ibm.mobile.jsonstoreofflinesync.manager.FlightAttendantManager;

import java.util.ArrayList;

//This activity will get the data from MyCustomAdapter and load into listview for displaying List of Food Menu Items
public class FoodMenuActivity extends AppCompatActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmenu);

        //Setting the title for this Page
        getSupportActionBar().setTitle("Menu Card");

        //This will get the food menu list from the singleton class
        ArrayList<FoodMenuItems> dataList = FlightAttendantManager.getInstance().getFoodMenuList();

        if(dataList.size()==0)
        Toast.makeText(this,R.string.pre_sync_check, Toast.LENGTH_SHORT).show();

        //Get the id of listview
        ListView listView = findViewById(R.id.foodmenu_listview);
        //Creating adapter object from the FoodMenuAdapter class
        FoodMenuAdapter adapter = new FoodMenuAdapter(this);
        //Pass the above collected data in datalist to the adapter of listview by calling setitems function in the MyCustomAdapter Class
        adapter.setItems(dataList);
        listView.setAdapter(adapter);

        }
}

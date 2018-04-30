package com.ibm.mobile.jsonstoreofflinesync.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.mobile.jsonstoreofflinesync.adapters.ShowSalesAdapter;
import com.ibm.mobile.jsonstoreofflinesync.manager.FlightAttendantManager;

import java.util.ArrayList;

/**
 * Created by abhilash_m on 05/03/18.
 */
public class ShowSalesActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsales);

        getSupportActionBar().setTitle("All Sales");

        //This will get the make sale list from the singleton class
        ArrayList<MakeSaleItems> saledataList = FlightAttendantManager.getInstance().getShowSalesList();

        if(saledataList.size()==0)
        Toast.makeText(this,R.string.pre_sync_check, Toast.LENGTH_SHORT).show();

        //Get the id of listview
        ListView listView = findViewById(R.id.showsales_listview);
        //Creating adapter object from the ShowSalesAdapter class which is for showing sales
        ShowSalesAdapter adapter = new ShowSalesAdapter(this);
        //Pass the above collected data in datalist to the adapter of listview by calling setitems function in the MyCustomAdapter Class
        adapter.setItems(saledataList);
        listView.setAdapter(adapter);

        }
}

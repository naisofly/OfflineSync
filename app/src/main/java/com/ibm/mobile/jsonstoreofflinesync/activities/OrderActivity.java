package com.ibm.mobile.jsonstoreofflinesync.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.jsonstoreofflinesync.manager.FlightAttendantManager;
import com.worklight.jsonstore.api.JSONStoreAddOptions;
import com.worklight.jsonstore.api.JSONStoreCollection;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {


    //Creating and initializing Object for making sale
    MakeSaleItems makesaleitem = new MakeSaleItems();

    //Creating a JSONObject as to add data to Collection
    JSONObject sale = new JSONObject();

    //These are declared to remember the selection
    private String seatNo;
    private String seatAlphabet;

    //This variable denotes quantity which is initially zero
    int minInteger = 0;

    //Getting the ids for every textview
    //These textviews are used to update the text content
    //For quantity
    TextView tvItemCount;
    //For TotalPrice
    TextView tvTotalPrice;
    //For Item Price
    TextView tvBasePrice;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Page title
        getSupportActionBar().setTitle("Order Details");


         tvItemCount= findViewById(R.id.txt_qty);
        //For TotalPrice
        tvTotalPrice= findViewById(R.id.total_price);
        //For Item Price
        tvBasePrice= findViewById(R.id.base_price);

        //For the purpose of checking the item name and the corresponding price to refer from Food Menu List
        final ArrayList<FoodMenuItems> checkFoodMenuList = FlightAttendantManager.getInstance().getFoodMenuList();
        if (checkFoodMenuList.size() == 0)
            Toast.makeText(this, R.string.pre_sync_check, Toast.LENGTH_SHORT).show();


        //Add only those items which have quantity more than 0 in the Food Menu List
        //This is for displaying items in drop down list
        final ArrayList<String> foodmenuitems = new ArrayList<>();
        ArrayList<String> showFoodList = new ArrayList<>();
        for (FoodMenuItems fooditem : checkFoodMenuList)
            if (!fooditem.getQuantity().equals("0")) {
                foodmenuitems.add(fooditem.getItem_name());
                showFoodList.add(fooditem.getItem_name() + "     $" + fooditem.getPrice());
            }


        //We get the spinner Ids for using drop down list for selecting seats
        Spinner seatDropDown1 = findViewById(R.id.spinner_seatrow);
        Spinner seatDropDown2 = findViewById(R.id.spinner_seatcolumn);
        Spinner itemDropDown = findViewById(R.id.spinner_item);





        //It is used to add total seats(34) which will be displayed in the drop down list of seat row selection
        final ArrayList<String> seatRow = new ArrayList<>();
        for (int i = 1; i < 35; i++)
            seatRow.add(Integer.toString(i));

        //It will be displayed in the drop down list of seat column selection
        final ArrayList<String> seatColumn = new ArrayList<>();
        for (char i='A';i<'G';i++)
            seatColumn.add(Character.toString(i));



        //Code for Seat Drop Down List(Both)
        //Add all the seat nos (Row) to the adapter which is the data which it takes for displaying on the list
        ArrayAdapter<String> seatrowAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, seatRow);
        seatDropDown1.setAdapter(seatrowAdapter);
        //For selecting an item this listener is called
        seatDropDown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                seatNo = seatRow.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Add all the seats (Column names) to the adapter which is the data which it takes for displaying on the list
        ArrayAdapter<String> seatcolumnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, seatColumn);
        seatDropDown2.setAdapter(seatcolumnAdapter);
        //For selecting an item this listener is called
        seatDropDown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                seatAlphabet = seatColumn.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });




        //Code for Item Drop Down List
        ArrayAdapter<String> itemAdapter= new  ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, showFoodList);
        itemDropDown.setAdapter(itemAdapter);
        itemDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Add the selected item name to the makesale object
                makesaleitem.item_name = foodmenuitems.get(position);
                //Checking the item in the food menu list and updating its corresponding price
                for (FoodMenuItems item: checkFoodMenuList)
                    if (foodmenuitems.get(position).equals(item.getItem_name()))
                        tvBasePrice.setText(item.getPrice());
                minInteger=1;
                display(minInteger);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Button btnIncrement = findViewById(R.id.btn_increment);
        //Increasing quantity on '+' button click
        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minInteger += 1;
                //Checking in the list of foodItems if the quantity is exceeded
                for (FoodMenuItems check:checkFoodMenuList)
                    if (check.getItem_name().equals(makesaleitem.item_name) && Integer.parseInt(check.getQuantity())<minInteger)
                    {
                        minInteger-=1;
                        Toast.makeText(getApplicationContext(),"No More "+makesaleitem.item_name,Toast.LENGTH_SHORT).show();
                    }

                display(minInteger);
            }
        });


        Button btnDecrement = findViewById(R.id.btn_decrement);
        //Decreasing Quantity(Min Value :0) on '-' button click
        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minInteger > 1)
                    minInteger -= 1;
                display(minInteger);
            }
        });

        //Creating a JSONStoreCollection for using the collection from FlightAttendantManager
        final JSONStoreCollection makesales =  FlightAttendantManager.getInstance().makesale;

         /*On Clicking this button it will check the current quantity, add it to the make sale object and every other entity
          will be added to the make sale collection */
        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add the selected seat to makesale object
                makesaleitem.seat_no = seatNo + seatAlphabet;
                //Add the quantity to makesale object
                makesaleitem.quantity = minInteger;
                //Decrement the quantity from foodmenu collection
                FlightAttendantManager.getInstance().removeQuantity(makesaleitem.item_name, minInteger);
                try {

                    //To indicate that data is not synced with backend
                    JSONStoreAddOptions options = new JSONStoreAddOptions();
                    options.setMarkDirty(true);

                    //Adding data to JSONObject
                    sale.put("quantity", makesaleitem.quantity);
                    sale.put("price", makesaleitem.price);
                    sale.put("item_name", makesaleitem.item_name);
                    sale.put("seat_no", makesaleitem.seat_no);

                    /*Adding data to the collection with the help of JSONObject(will consider strings with space as well),
                    this will do the upstream sync, i.e. the data added here will be added to the remote CloudantDB*/
                    makesales.addData(sale, options);


                    //Adding data for displaying on the sales data
                    FlightAttendantManager.getInstance().addMakeSaleItems(makesaleitem);


                    Toast.makeText(getApplicationContext(), "Order Successfully Placed!", Toast.LENGTH_SHORT).show();
                    //Will go to previous activity in the stack(Use this to retain the flight no shown on main page)
                    finish();

                } catch (Exception e) {
                    System.out.println("Exception occurred : " + e.getMessage());
                }
                Log.d("Make_Sale_Item", makesaleitem.seat_no + " " + makesaleitem.item_name + " " + makesaleitem.quantity + " " + makesaleitem.price);
                }
            });


    }
    private void display(int number)
    {
        //Display quantity
        tvItemCount.setText(Integer.toString(number));
        //Calculate and display total price
        tvTotalPrice.setText(Float.toString((float)(number * Integer.parseInt(tvBasePrice.getText().toString()))));
        //Add data to make sale object
        makesaleitem.quantity = number;
        makesaleitem.price = tvTotalPrice.getText().toString();
    }

}



package com.ibm.mobile.jsonstoreofflinesync.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bumptech.glide.Glide;

import com.ibm.mobile.jsonstoreofflinesync.activities.R;
import com.ibm.mobile.jsonstoreofflinesync.activities.MakeSaleItems;
import com.ibm.mobile.jsonstoreofflinesync.activities.FoodMenuItems;

import java.util.ArrayList;


/**
 * Created by abhilash_m on 09/02/18.
 */

public class FoodMenuAdapter extends BaseAdapter{


    private Context mContext;

    public FoodMenuAdapter(Context context)
    {
        mContext=context;
    }
    //It is the array of list objects of type FoodMenuItems
    ArrayList<FoodMenuItems> itemList = new ArrayList<>();

    //This will update the itemList
    public void setItems(ArrayList<FoodMenuItems> itemList){
        this.itemList = itemList;
    }

    //This will return the no of items which will be displayed as each card in the list
    @Override
    public int getCount() {
        return itemList.size();
    }

    //Returns an individual item
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //This function returns the View in which we have updated the data.
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //It will inflate the layout to single object of the list
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        View listItemView = layoutInflator.inflate(R.layout.list_view_item, viewGroup, false);

        //Setting the price of list items in the listView
        TextView price_label = listItemView.findViewById(R.id.tvprice);
        TextView price_values = listItemView.findViewById(R.id.edit_price);
        price_values.setText(itemList.get(position).getPrice());

        //Setting the quantity of list items in the listView
        TextView quantity_label = listItemView.findViewById(R.id.tvqty);
        TextView quantity_value = listItemView.findViewById(R.id.edit_qty);
        quantity_value.setText(itemList.get(position).getQuantity());

        ImageView ivImageItems = listItemView.findViewById(R.id.meal_image);

        //To convert URL to image use Glide function
        Glide.with(mContext)
                .load(itemList.get(position).getImage_url())
                .into(ivImageItems);

        //Setting the names of items in the listView
        TextView meal_names = listItemView.findViewById(R.id.meal_title);
        meal_names.setText(itemList.get(position).getItem_name());

        //If Item not available show the whole card in Grey Color
        if(quantity_value.getText().equals("0")) {
            meal_names.setTextColor(Color.LTGRAY);
            quantity_value.setTextColor(Color.LTGRAY);
            quantity_label.setTextColor(Color.LTGRAY);
            price_label.setTextColor(Color.LTGRAY);
            price_values.setTextColor(Color.LTGRAY);
            //Below code is to make the image grey
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0f);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            ivImageItems.setAlpha(0.5F);
            ivImageItems.setColorFilter(filter);
        }
        return listItemView;
    }
}


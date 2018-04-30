package com.ibm.mobile.jsonstoreofflinesync.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ibm.mobile.jsonstoreofflinesync.activities.MakeSaleItems;
import com.ibm.mobile.jsonstoreofflinesync.activities.R;

import java.util.ArrayList;

//This Adapter is used for passing Values to ShowSales page
public class ShowSalesAdapter extends BaseAdapter {

    private Context mContext;

    public ShowSalesAdapter(Context context)
    {
        mContext=context;
    }

    //It is the array of list objects of type MakeSaleItems
    ArrayList<MakeSaleItems> showsalesList = new ArrayList<>();

    //This will update the itemList
    public void setItems(ArrayList<MakeSaleItems> showSalesList){
        this.showsalesList = showSalesList;
    }

    @Override
    public int getCount() {
        return showsalesList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.showsalesList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //It will inflate the layout to single object of the list
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        View listItemView = layoutInflator.inflate(R.layout.list_view_sales, viewGroup, false);

        //Setting the seat no of items in the listView
        TextView displayseat = listItemView.findViewById(R.id.sale_seat);
        displayseat.setText(showsalesList.get(position).getSeat_no());

        TextView displayitem = listItemView.findViewById(R.id.sale_item);
        displayitem.setText(showsalesList.get(position).getItem_name());

        TextView displayquantity= listItemView.findViewById(R.id.sale_quantity);
        displayquantity.setText(Integer.toString(showsalesList.get(position).getQuantity()));

        return listItemView;
    }
}

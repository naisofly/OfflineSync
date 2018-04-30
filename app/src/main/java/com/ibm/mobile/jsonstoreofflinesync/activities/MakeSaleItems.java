package com.ibm.mobile.jsonstoreofflinesync.activities;

//This is for creating objects containing values for item name,seat no , quantity and price for the data which goes to the CloudantDB
public class MakeSaleItems {

    String item_name;
    String seat_no;
    int quantity;
    String price;

    public String getItem_name() {
        return item_name;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }
}

package com.ibm.mobile.jsonstoreofflinesync.activities;

//This is for creating objects containing values for item name , quantity,price and image url for the data which comes from the Cloudant DB
public class FoodMenuItems{

    String item_name;
    String quantity;
    String price;
    String image_url;

    public FoodMenuItems(String item_name,String quantity,String price,String image_url)
    {
        this.item_name=item_name;
        this.quantity=quantity;
        this.price=price;
        this.image_url=image_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getImage_url() {
        return image_url;
    }
}

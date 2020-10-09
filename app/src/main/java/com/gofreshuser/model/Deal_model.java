package com.gofreshuser.model;

public class Deal_model {

String product_name;
String product_image;
String deal_price;
String unit_value;
String product_id;
String price;
String time_interval;
String unit;


    String mrp;

String product_description;

    public String getTime_interval() {
        return time_interval;
    }
    public String getMrp() {
        return mrp;
    }

    public void setTime_interval(String time_interval) {
        this.time_interval = time_interval;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public String getUnit_value() {
        return unit_value;
    }
}

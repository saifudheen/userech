package com.gofreshuser.model;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class My_Past_order_model {

    String sale_id;
    String user_id;
    String on_date;
    String delivery_time_from;
    String delivery_time_to;
    String status;
    String note;
    String is_paid;
    String total_amount;
    String total_kg;
    String vandor_name;
    String total_items;
    String socity_id;
    String delivery_address;
    String location_id;
    String delivery_charge;
    String payment_method;
    String used_amount;
    String coupon_code;

    public String getUsed_amount() {
        return used_amount;
    }

    public void setUsed_amount(String used_amount) {
        this.used_amount = used_amount;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getPrevious_amount() {
        return previous_amount;
    }

    public void setPrevious_amount(String previous_amount) {
        this.previous_amount = previous_amount;
    }

    String previous_amount;

    public String getSale_id(){
        return sale_id;
    }

    public String getUser_id(){
        return user_id;
    }

    public String getOn_date(){
        return on_date;
    }

    public String getDelivery_time_from(){
        return delivery_time_from;
    }

    public String getVandor_name() {
        return vandor_name;
    }

    public String getDelivery_time_to(){
        return delivery_time_to;
    }

    public String getStatus(){
        return status;
    }

    public String getNote(){
        return note;
    }

    public String getIs_paid(){
        return is_paid;
    }

    public String getTotal_amount(){
        return total_amount;
    }

    public String getTotal_kg(){
        return total_kg;
    }

    public String getTotal_items(){
        return total_items;
    }

    public String getSocity_id(){
        return socity_id;
    }

    public String getDelivery_address(){
        return delivery_address;
    }

    public String getLocation_id(){
        return location_id;
    }

    public String getDelivery_charge(){
        return delivery_charge;
    }
    public String getPayment_method() {
        return payment_method;
    }


}

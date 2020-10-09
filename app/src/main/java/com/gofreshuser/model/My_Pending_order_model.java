package com.gofreshuser.model;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class My_Pending_order_model {

    String assgin_to;
    String lat;
    String delivery_boy_name;

    public String getVandor_name() {
        return vandor_name;
    }

    public void setVandor_name(String vandor_name) {
        this.vandor_name = vandor_name;
    }

    String delivery_number;
    String vandor_name;
    String delivery_boy_lat;
    String delivery_boy_lng;

    public String getDelivery_boy_name() {
        return delivery_boy_name;
    }

    public void setDelivery_boy_name(String delivery_boy_name) {
        this.delivery_boy_name = delivery_boy_name;
    }

    public String getDelivery_number() {
        return delivery_number;
    }

    public void setDelivery_number(String delivery_number) {
        this.delivery_number = delivery_number;
    }

    public String getDelivery_boy_lat() {
        return delivery_boy_lat;
    }

    public void setDelivery_boy_lat(String delivery_boy_lat) {
        this.delivery_boy_lat = delivery_boy_lat;
    }

    public String getDelivery_boy_lng() {
        return delivery_boy_lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setDelivery_boy_lng(String delivery_boy_lng) {
        this.delivery_boy_lng = delivery_boy_lng;
    }



    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setOn_date(String on_date) {
        this.on_date = on_date;
    }

    public void setDelivery_time_from(String delivery_time_from) {
        this.delivery_time_from = delivery_time_from;
    }

    public void setDelivery_time_to(String delivery_time_to) {
        this.delivery_time_to = delivery_time_to;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public void setTotal_kg(String total_kg) {
        this.total_kg = total_kg;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getAssgin_to() {
        return assgin_to;
    }

    public void setAssgin_to(String assgin_to) {
        this.assgin_to = assgin_to;
    }




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
    String total_items;
    String socity_id;
    String lng;
    String delivery_address;
    String location_id;
    String delivery_charge;

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
    String payment_method;

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

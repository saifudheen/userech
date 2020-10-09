package com.gofreshuser.model;

public class Charges_model {

    String charge_id;
    String cart_from;
    String cart_to;
    String charge_amount;

    public String getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
    }

    public String getCart_from() {
        return cart_from;
    }

    public void setCart_from(String cart_from) {
        this.cart_from = cart_from;
    }

    public String getCart_to() {
        return cart_to;
    }

    public void setCart_to(String cart_to) {
        this.cart_to = cart_to;
    }

    public String getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(String charge_amount) {
        this.charge_amount = charge_amount;
    }
}

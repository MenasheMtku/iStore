package com.example.istore.Model;

public class CheckoutModel {

    String total_items,ships_to,total_amount,order_date,user_name, order_id,user_id;

    public CheckoutModel() {
    }

    public CheckoutModel(String total_items, String ships_to, String total_amount,
                         String order_date, String user_name, String order_id,String user_id) {
        this.total_items = total_items;
        this.ships_to = ships_to;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.user_name = user_name;
        this.order_id = order_id;
        this.user_id = user_id;

    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getShips_to() {
        return ships_to;
    }

    public void setShips_to(String ships_to) {
        this.ships_to = ships_to;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }
}

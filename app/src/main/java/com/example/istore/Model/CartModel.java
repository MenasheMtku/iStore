package com.example.istore.Model;

public class CartModel {

    String  id ,name , date ,time,price,totalQuantity,totalPrice , orderID ;
    boolean hasPaid = false;

    public CartModel() {}

    public CartModel(String id,String name, String date, String time,String orderID,
                     String price, String totalQuantity, String totalPrice,
                     boolean hasPaid) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.hasPaid = hasPaid;
        this.orderID = orderID;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public String getOrderID() {
        return orderID;
    }
}

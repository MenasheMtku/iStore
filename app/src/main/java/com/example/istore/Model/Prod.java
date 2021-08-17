package com.example.istore.Model;

public class Prod {

    private String id;
    private String name;
    private String quantity;
    private String expiry;

    public Prod() {
    }

    public Prod(String id,String name, String quantity, String expiry) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiry = expiry;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}

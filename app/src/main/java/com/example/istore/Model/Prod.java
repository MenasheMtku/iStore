package com.example.istore.Model;

public class Prod {


    private String id , name, quantity, expiry, imageUrl, catName;

    public Prod() {
    }

    public Prod(String id, String name, String quantity, String expiry, String imageUrl, String catName ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiry = expiry;
        this.imageUrl = imageUrl;
        this.catName = catName;

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
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}

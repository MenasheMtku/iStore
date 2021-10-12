package com.example.istore.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProdModel {

    private String id, name, price, expiry, imageUrl, category, description;
            String quantity;

    public ProdModel() {
    }

    public ProdModel(String id, String name, String quantity, String expiry, String imageUrl, String category , String price , String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.expiry = expiry;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;

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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    @Nullable
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
//    @Override
//    public String toString() {
//        return new StringBuilder().append("ProdModel{")
//                                    .append('\n'+"id = ' ").append(id).append('\n')
//                                    .append("name = ' ").append(name).append('\'')
//                                    .append("quantity = '").append(quantity).append('\'')
//                                    .append("expiry = '").append(expiry).append('\n')
//                                    .append("imageUrl = '").append(imageUrl).append('\n')
//                                    .append("catName = '").append(category).append('\n')
//                                    .append("price = '").append(price).append('\n')
//                                    .append("description = '").append(description)
//                                    .append('\n').append('}').toString();
//    }

}

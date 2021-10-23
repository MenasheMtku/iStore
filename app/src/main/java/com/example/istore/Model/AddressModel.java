package com.example.istore.Model;

public class AddressModel extends CartModel {

    String  name, phone,postalCode;
    String  address;
    boolean isSelected;

    public AddressModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AddressModel(String name, String phone, String postalCode, String address, boolean isSelected) {
        this.name = name;
        this.phone = phone;
        this.postalCode = postalCode;
        this.address = address;
        this.isSelected = isSelected;


    }

    //    public AddressModel(String userName, String userAddress, boolean isSelected) {
//        this.userName = userName;
//        this.userAddress = userAddress;
//        this.isSelected = isSelected;
//
//    }
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserAddress() {
//        return userAddress;
//    }
//
//    public void setUserAddress(String userAddress) {
//        this.userAddress = userAddress;
//    }
//
//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }
}

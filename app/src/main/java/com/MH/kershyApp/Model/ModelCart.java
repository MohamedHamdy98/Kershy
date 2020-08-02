package com.MH.kershyApp.Model;

public class ModelCart {
    private String id;
    public String name;
    public String numItem;
    public String price;
    public String  totalPrice;
    private boolean isShrink = false;

    public ModelCart(String name, String numItem, String price) {
        this.name = name;
        this.numItem = numItem;
        this.price = price;
    }

    public ModelCart(String name, String numItem, String price, String totalPrice) {
        this.name = name;
        this.numItem = numItem;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public ModelCart() {
    }


    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getNumItem() {
        return numItem;
    }

    public void setNumItem(String numItem) {
        this.numItem = numItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



}

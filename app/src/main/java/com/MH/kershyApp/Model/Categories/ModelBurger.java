package com.MH.kershyApp.Model.Categories;

public class ModelBurger {
    private String name_burger;
    private String description_burger;
    private String price_burger;
    private String totalPrice_burger;
    private String numberItem_burger;
    private String Image_burger;
    private boolean isShrink = true;

    public ModelBurger(String name_burger, String description_burger, String price_burger, String image_burger) {
        this.name_burger = name_burger;
        this.description_burger = description_burger;
        this.price_burger = price_burger;
        Image_burger = image_burger;
    }

    public ModelBurger(String name_burger, String description_burger, String price_burger, String totalPrice_burger, String image_burger) {
        this.name_burger = name_burger;
        this.description_burger = description_burger;
        this.price_burger = price_burger;
        this.totalPrice_burger = totalPrice_burger;
        Image_burger = image_burger;
    }

    public ModelBurger() {
    }

    public String getTotalPrice_burger() {
        return totalPrice_burger;
    }

    public void setTotalPrice_burger(String totalPrice_burger) {
        this.totalPrice_burger = totalPrice_burger;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public String getName_burger() {
        return name_burger;
    }

    public void setName_burger(String name_burger) {
        this.name_burger = name_burger;
    }

    public String getDescription_burger() {
        return description_burger;
    }

    public void setDescription_burger(String description_burger) {
        this.description_burger = description_burger;
    }

    public String getImage_burger() {
        return Image_burger;
    }

    public void setImage_burger(String image_burger) {
        Image_burger = image_burger;
    }

    public String getPrice_burger() {
        return price_burger;
    }

    public void setPrice_burger(String price_burger) {
        this.price_burger = price_burger;
    }

}

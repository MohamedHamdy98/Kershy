package com.MH.kershyApp.Model.Categories;

public class ModelSweet {
    private String name_sweet;
    private String description_sweet;
    private String price_sweet;
    private String Image_sweet;
    private boolean isShrink = true;

    public ModelSweet() {
    }

    public ModelSweet(String name_sweet, String description_sweet, String price_sweet, String image_sweet) {
        this.name_sweet = name_sweet;
        this.description_sweet = description_sweet;
        this.price_sweet = price_sweet;
        Image_sweet = image_sweet;
    }

    public String getName_sweet() {
        return name_sweet;
    }

    public void setName_sweet(String name_sweet) {
        this.name_sweet = name_sweet;
    }

    public String getDescription_sweet() {
        return description_sweet;
    }

    public void setDescription_sweet(String description_sweet) {
        this.description_sweet = description_sweet;
    }

    public String getImage_sweet() {
        return Image_sweet;
    }

    public void setImage_sweet(String image_sweet) {
        Image_sweet = image_sweet;
    }

    public String getPrice_sweet() {
        return price_sweet;
    }

    public void setPrice_sweet(String price_sweet) {
        this.price_sweet = price_sweet;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }
}

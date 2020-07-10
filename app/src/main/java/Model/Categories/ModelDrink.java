package Model.Categories;

public class ModelDrink {
    private String name_drink;
    private String description_drink;
    private String price_drink;
    private String Image_drink;
    private boolean isShrink = true;

    public ModelDrink() {
    }

    public ModelDrink(String name_drink, String description_drink, String price_drink, String image_drink) {
        this.name_drink = name_drink;
        this.description_drink = description_drink;
        this.price_drink = price_drink;
        Image_drink = image_drink;
    }

    public String getName_drink() {
        return name_drink;
    }

    public void setName_drink(String name_drink) {
        this.name_drink = name_drink;
    }

    public String getDescription_drink() {
        return description_drink;
    }

    public void setDescription_drink(String description_drink) {
        this.description_drink = description_drink;
    }

    public String getPrice_drink() {
        return price_drink;
    }

    public void setPrice_drink(String price_drink) {
        this.price_drink = price_drink;
    }

    public String getImage_drink() {
        return Image_drink;
    }

    public void setImage_drink(String image_drink) {
        Image_drink = image_drink;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }
}

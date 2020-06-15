package Model;

public class ModelCategory {
    private String name_category;
    private String description;
    private int Image_category;
    private String price;
    private boolean isShrink = true;

    public ModelCategory(String name_category, String description,String price,int image_category) {
        this.name_category = name_category;
        this.description = description;
        this.price = price;
        Image_category = image_category;
    }

    public ModelCategory() {
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public int getImage_category() {
        return Image_category;
    }

    public void setImage_category(int image_category) {
        Image_category = image_category;
    }
}

package Model;

public class ModelCart {
    private String name_cart;
    private String price;
    private String number_item;

    public ModelCart(String name_cart, String price, String number_item) {
        this.name_cart = name_cart;
        this.price = price;
        this.number_item = number_item;
    }

    public ModelCart() {
    }

    public String getName_cart() {
        return name_cart;
    }

    public void setName_cart(String name_cart) {
        this.name_cart = name_cart;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber_item() {
        return number_item;
    }

    public void setNumber_item(String number_item) {
        this.number_item = number_item;
    }
}

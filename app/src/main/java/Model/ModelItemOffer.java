package Model;

public class ModelItemOffer {
    private String name_offer;
    private String old_price_offer;
    private String new_price_offer;
    private String image_offer;

    public ModelItemOffer() {
    }

    public ModelItemOffer(String name_offer, String old_price_offer, String new_price_offer, String image_offer) {
        this.name_offer = name_offer;
        this.old_price_offer = old_price_offer;
        this.new_price_offer = new_price_offer;
        this.image_offer = image_offer;
    }

    public String getName_offer() {
        return name_offer;
    }

    public void setName_offer(String name_offer) {
        this.name_offer = name_offer;
    }

    public String getOld_price_offer() {
        return old_price_offer;
    }

    public void setOld_price_offer(String old_price_offer) {
        this.old_price_offer = old_price_offer;
    }

    public String getNew_price_offer() {
        return new_price_offer;
    }

    public void setNew_price_offer(String new_price_offer) {
        this.new_price_offer = new_price_offer;
    }

    public String getImage_offer() {
        return image_offer;
    }

    public void setImage_offer(String image_offer) {
        this.image_offer = image_offer;
    }
}

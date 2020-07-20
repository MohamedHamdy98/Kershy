package Model;

public class User {
    public int num;
    public String nameUser;
    private String imageURL;
    private String phone;

    public User() {
    }

    public User(String nameUser, String imageURL, String phone) {
        this.nameUser = nameUser;
        this.imageURL = imageURL;
        this.phone = phone;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

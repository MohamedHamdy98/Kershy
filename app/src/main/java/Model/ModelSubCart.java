package Model;

import java.util.ArrayList;

public class ModelSubCart {
    private String title;
    private ArrayList<ModelCart> modelCartsList;

    public ModelSubCart() {
    }

    public ModelSubCart(String title, ArrayList<ModelCart> modelCartsList) {
        this.title = title;
        this.modelCartsList = modelCartsList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ModelCart> getModelCartsList() {
        return modelCartsList;
    }

    public void setModelCartsList(ArrayList<ModelCart> modelCartsList) {
        this.modelCartsList = modelCartsList;
    }
}

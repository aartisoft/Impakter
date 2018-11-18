package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductObj {
    public ProductObj(int id, String name, String brand, float price) {
        this.price = price;
        this.brand = brand;
        this.name = name;
        this.id = id;
    }

    @Expose
    @SerializedName("price")
    private float price;
    @Expose
    @SerializedName("brand")
    private String brand;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("id")
    private int id;

    public float getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

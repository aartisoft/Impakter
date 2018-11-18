package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductByCategoryRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("subCatList")
    private List<SubCatList> subCatList;
    @Expose
    @SerializedName("allPages")
    private int allPages;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<SubCatList> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<SubCatList> subCatList) {
        this.subCatList = subCatList;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("rating")
        private float rating;
        @Expose
        @SerializedName("price")
        private double price;
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
        @SerializedName("catId")
        private int catId;
        @Expose
        @SerializedName("id")
        private int id;

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
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

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class SubCatList {
        @Expose
        @SerializedName("countProduct")
        private String countProduct;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public String getCountProduct() {
            return countProduct;
        }

        public void setCountProduct(String countProduct) {
            this.countProduct = countProduct;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

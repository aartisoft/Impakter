package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollectionRespond {


    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<CollectionObj> data;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CollectionObj> getData() {
        return data;
    }

    public void setData(List<CollectionObj> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("totalFavorite")
        private int totalFavorite;
        @Expose
        @SerializedName("listProduct")
        private List<ListProduct> listProduct;
        @Expose
        @SerializedName("collectionName")
        private String collectionName;
        @Expose
        @SerializedName("id")
        private int id;

        public int getTotalFavorite() {
            return totalFavorite;
        }

        public void setTotalFavorite(int totalFavorite) {
            this.totalFavorite = totalFavorite;
        }

        public List<ListProduct> getListProduct() {
            return listProduct;
        }

        public void setListProduct(List<ListProduct> listProduct) {
            this.listProduct = listProduct;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class ListProduct {
        @Expose
        @SerializedName("productImage")
        private String productImage;
        @Expose
        @SerializedName("productName")
        private String productName;
        @Expose
        @SerializedName("productId")
        private int productId;

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }
    }
}

package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HomeCategoryRespond {


    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<Data> data;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("listProduct")
        private List<ProductObj> listProduct;
        @Expose
        @SerializedName("subCatArray")
        private List<SubCatArray> subCatArray;
        @Expose
        @SerializedName("categoryImage")
        private String categoryImage;
        @Expose
        @SerializedName("categoryName")
        private String categoryName;
        @Expose
        @SerializedName("categoryId")
        private int categoryId;

        public List<ProductObj> getListProduct() {
            return listProduct;
        }

        public void setListProduct(List<ProductObj> listProduct) {
            this.listProduct = listProduct;
        }

        public List<SubCatArray> getSubCatArray() {
            return subCatArray;
        }

        public void setSubCatArray(List<SubCatArray> subCatArray) {
            this.subCatArray = subCatArray;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static class ListProduct {
        @Expose
        @SerializedName("price")
        private int price;
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

        public int getPrice() {
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

    public static class SubCatArray implements Parcelable{
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;
        @SerializedName("parentId")
        private int parentId;


        protected SubCatArray(Parcel in) {
            image = in.readString();
            name = in.readString();
            id = in.readInt();
            parentId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(image);
            dest.writeString(name);
            dest.writeInt(id);
            dest.writeInt(parentId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SubCatArray> CREATOR = new Creator<SubCatArray>() {
            @Override
            public SubCatArray createFromParcel(Parcel in) {
                return new SubCatArray(in);
            }

            @Override
            public SubCatArray[] newArray(int size) {
                return new SubCatArray[size];
            }
        };

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
    }
}

package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRespond {

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
        @SerializedName("arrOrder")
        private List<ArrOrder> arrOrder;
        @Expose
        @SerializedName("orderNumber")
        private int orderNumber;

        public List<ArrOrder> getArrOrder() {
            return arrOrder;
        }

        public void setArrOrder(List<ArrOrder> arrOrder) {
            this.arrOrder = arrOrder;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }
    }

    public static class ArrOrder {
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("time")
        private String time;
        @Expose
        @SerializedName("brandName")
        private String brandName;
        @Expose
        @SerializedName("brandId")
        private int brandId;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("total")
        private float total;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("id")
        private int id;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public int getBrandId() {
            return brandId;
        }

        public void setBrandId(int brandId) {
            this.brandId = brandId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getTotal() {
            return total;
        }

        public void setTotal(float total) {
            this.total = total;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

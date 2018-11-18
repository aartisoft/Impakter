package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
        @SerializedName("listItems")
        private List<ListItems> listItems;
        @Expose
        @SerializedName("shippingAddress")
        private String shippingAddress;
        @SerializedName("shipmentImage")
        private String shipmentImage;
        @Expose
        @SerializedName("shipmentInformation")
        private String shipmentInformation;
        @Expose
        @SerializedName("orderDate")
        private String orderDate;
        @Expose
        @SerializedName("totalOrderPrice")
        private float totalOrderPrice;
        @Expose
        @SerializedName("tax")
        private float tax;
        @Expose
        @SerializedName("shippingPrice")
        private float shippingPrice;
        @Expose
        @SerializedName("subTotalOrderPrice")
        private float subTotalOrderPrice;
        @Expose
        @SerializedName("itemsQuantity")
        private int itemsQuantity;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("brandName")
        private String brandName;
        @Expose
        @SerializedName("brandId")
        private int brandId;
        @Expose
        @SerializedName("orderId")
        private int orderId;

        public List<ListItems> getListItems() {
            return listItems;
        }

        public void setListItems(List<ListItems> listItems) {
            this.listItems = listItems;
        }

        public String getShipmentImage() {
            return shipmentImage;
        }

        public void setShipmentImage(String shipmentImage) {
            this.shipmentImage = shipmentImage;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getShipmentInformation() {
            return shipmentInformation;
        }

        public void setShipmentInformation(String shipmentInformation) {
            this.shipmentInformation = shipmentInformation;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public float getTotalOrderPrice() {
            return totalOrderPrice;
        }

        public void setTotalOrderPrice(float totalOrderPrice) {
            this.totalOrderPrice = totalOrderPrice;
        }

        public float getTax() {
            return tax;
        }

        public void setTax(float tax) {
            this.tax = tax;
        }

        public float getShippingPrice() {
            return shippingPrice;
        }

        public void setShippingPrice(float shippingPrice) {
            this.shippingPrice = shippingPrice;
        }

        public float getSubTotalOrderPrice() {
            return subTotalOrderPrice;
        }

        public void setSubTotalOrderPrice(float subTotalOrderPrice) {
            this.subTotalOrderPrice = subTotalOrderPrice;
        }

        public int getItemsQuantity() {
            return itemsQuantity;
        }

        public void setItemsQuantity(int itemsQuantity) {
            this.itemsQuantity = itemsQuantity;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
    }

    public static class ListItems implements Parcelable {
        @SerializedName("rate")
        private float rate;
        @Expose
        @SerializedName("totalPrice")
        private float totalPrice;
        @Expose
        @SerializedName("options")
        private String options;
        @Expose
        @SerializedName("discount")
        private float discount;
        @Expose
        @SerializedName("quantity")
        private int quantity;
        @Expose
        @SerializedName("price")
        private float price;
        @Expose
        @SerializedName("code")
        private String code;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("orderItemId")
        private int orderItemId;

        protected ListItems(Parcel in) {
            rate = in.readFloat();
            totalPrice = in.readFloat();
            options = in.readString();
            discount = in.readFloat();
            quantity = in.readInt();
            price = in.readFloat();
            code = in.readString();
            image = in.readString();
            name = in.readString();
            id = in.readInt();
            orderItemId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(rate);
            dest.writeFloat(totalPrice);
            dest.writeString(options);
            dest.writeFloat(discount);
            dest.writeInt(quantity);
            dest.writeFloat(price);
            dest.writeString(code);
            dest.writeString(image);
            dest.writeString(name);
            dest.writeInt(id);
            dest.writeInt(orderItemId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ListItems> CREATOR = new Creator<ListItems>() {
            @Override
            public ListItems createFromParcel(Parcel in) {
                return new ListItems(in);
            }

            @Override
            public ListItems[] newArray(int size) {
                return new ListItems[size];
            }
        };

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public float getDiscount() {
            return discount;
        }

        public void setDiscount(float discount) {
            this.discount = discount;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

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

        public int getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(int orderItemId) {
            this.orderItemId = orderItemId;
        }
    }
}

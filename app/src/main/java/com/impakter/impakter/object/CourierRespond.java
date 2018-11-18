package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourierRespond {

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
        @SerializedName("expedited")
        private Expedited expedited;
        @Expose
        @SerializedName("standard")
        private Standard standard;
        @Expose
        @SerializedName("region_shipping_id")
        private int region_shipping_id;
        @Expose
        @SerializedName("type_shipping_model")
        private int type_shipping_model;
        @Expose
        @SerializedName("brandId")
        private int brandId;
        @Expose
        @SerializedName("brandName")
        private String brandName;

        public Expedited getExpedited() {
            return expedited;
        }

        public void setExpedited(Expedited expedited) {
            this.expedited = expedited;
        }

        public Standard getStandard() {
            return standard;
        }

        public void setStandard(Standard standard) {
            this.standard = standard;
        }

        public int getRegion_shipping_id() {
            return region_shipping_id;
        }

        public void setRegion_shipping_id(int region_shipping_id) {
            this.region_shipping_id = region_shipping_id;
        }

        public int getType_shipping_model() {
            return type_shipping_model;
        }

        public void setType_shipping_model(int type_shipping_model) {
            this.type_shipping_model = type_shipping_model;
        }

        public int getBrandId() {
            return brandId;
        }

        public void setBrandId(int brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
    }

    public static class Expedited {
        @Expose
        @SerializedName("total")
        private Total total;
        @Expose
        @SerializedName("shipmentCharge")
        private ShipmentCharge shipmentCharge;
        @Expose
        @SerializedName("taxAndDutiesChange")
        private TaxAndDutiesChange taxAndDutiesChange;
        @Expose
        @SerializedName("deliveryTime")
        private String deliveryTime;
        @Expose
        @SerializedName("pickupAvailable")
        private String pickupAvailable;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public Total getTotal() {
            return total;
        }

        public void setTotal(Total total) {
            this.total = total;
        }

        public ShipmentCharge getShipmentCharge() {
            return shipmentCharge;
        }

        public void setShipmentCharge(ShipmentCharge shipmentCharge) {
            this.shipmentCharge = shipmentCharge;
        }

        public TaxAndDutiesChange getTaxAndDutiesChange() {
            return taxAndDutiesChange;
        }

        public void setTaxAndDutiesChange(TaxAndDutiesChange taxAndDutiesChange) {
            this.taxAndDutiesChange = taxAndDutiesChange;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getPickupAvailable() {
            return pickupAvailable;
        }

        public void setPickupAvailable(String pickupAvailable) {
            this.pickupAvailable = pickupAvailable;
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
    }

    public static class Total {
        @Expose
        @SerializedName("formatPrice")
        private String formatPrice;
        @Expose
        @SerializedName("price")
        private int price;

        public String getFormatPrice() {
            return formatPrice;
        }

        public void setFormatPrice(String formatPrice) {
            this.formatPrice = formatPrice;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static class ShipmentCharge {
        @Expose
        @SerializedName("formatPrice")
        private String formatPrice;
        @Expose
        @SerializedName("price")
        private int price;

        public String getFormatPrice() {
            return formatPrice;
        }

        public void setFormatPrice(String formatPrice) {
            this.formatPrice = formatPrice;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static class TaxAndDutiesChange {
        @Expose
        @SerializedName("formatPrice")
        private String formatPrice;
        @Expose
        @SerializedName("price")
        private int price;

        public String getFormatPrice() {
            return formatPrice;
        }

        public void setFormatPrice(String formatPrice) {
            this.formatPrice = formatPrice;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static class Standard {
        @Expose
        @SerializedName("total")
        private Total total;
        @Expose
        @SerializedName("shipmentCharge")
        private ShipmentCharge shipmentCharge;
        @Expose
        @SerializedName("taxAndDutiesChange")
        private TaxAndDutiesChange taxAndDutiesChange;
        @Expose
        @SerializedName("deliveryTime")
        private String deliveryTime;
        @Expose
        @SerializedName("pickupAvailable")
        private String pickupAvailable;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public Total getTotal() {
            return total;
        }

        public void setTotal(Total total) {
            this.total = total;
        }

        public ShipmentCharge getShipmentCharge() {
            return shipmentCharge;
        }

        public void setShipmentCharge(ShipmentCharge shipmentCharge) {
            this.shipmentCharge = shipmentCharge;
        }

        public TaxAndDutiesChange getTaxAndDutiesChange() {
            return taxAndDutiesChange;
        }

        public void setTaxAndDutiesChange(TaxAndDutiesChange taxAndDutiesChange) {
            this.taxAndDutiesChange = taxAndDutiesChange;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getPickupAvailable() {
            return pickupAvailable;
        }

        public void setPickupAvailable(String pickupAvailable) {
            this.pickupAvailable = pickupAvailable;
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
    }

}

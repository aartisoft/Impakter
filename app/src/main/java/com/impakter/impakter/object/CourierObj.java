package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

public class CourierObj implements Parcelable{
    private String type;
    private int id;
    private int brandId;
    private String typeName;
    private String image;
    private String pickupAvailable;
    private String deliveryTime;
    private float taxDuties;
    private float shipmentCharge;
    private float total;
    private int regionShippingId;
    private boolean isCheck = false;

    public CourierObj() {
    }

    protected CourierObj(Parcel in) {
        type = in.readString();
        id = in.readInt();
        brandId = in.readInt();
        typeName = in.readString();
        image = in.readString();
        pickupAvailable = in.readString();
        deliveryTime = in.readString();
        taxDuties = in.readFloat();
        shipmentCharge = in.readFloat();
        total = in.readFloat();
        regionShippingId = in.readInt();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeInt(brandId);
        dest.writeString(typeName);
        dest.writeString(image);
        dest.writeString(pickupAvailable);
        dest.writeString(deliveryTime);
        dest.writeFloat(taxDuties);
        dest.writeFloat(shipmentCharge);
        dest.writeFloat(total);
        dest.writeInt(regionShippingId);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourierObj> CREATOR = new Creator<CourierObj>() {
        @Override
        public CourierObj createFromParcel(Parcel in) {
            return new CourierObj(in);
        }

        @Override
        public CourierObj[] newArray(int size) {
            return new CourierObj[size];
        }
    };

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getRegionShippingId() {
        return regionShippingId;
    }

    public void setRegionShippingId(int regionShippingId) {
        this.regionShippingId = regionShippingId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPickupAvailable() {
        return pickupAvailable;
    }

    public void setPickupAvailable(String pickupAvailable) {
        this.pickupAvailable = pickupAvailable;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public float getTaxDuties() {
        return taxDuties;
    }

    public void setTaxDuties(float taxDuties) {
        this.taxDuties = taxDuties;
    }

    public float getShipmentCharge() {
        return shipmentCharge;
    }

    public void setShipmentCharge(float shipmentCharge) {
        this.shipmentCharge = shipmentCharge;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

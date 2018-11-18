package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShipmentObj implements Parcelable {
    private String brandName;
    private int brandId;
    private List<CourierObj> listCourier;

    public ShipmentObj() {
    }

    protected ShipmentObj(Parcel in) {
        brandName = in.readString();
        brandId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeInt(brandId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentObj> CREATOR = new Creator<ShipmentObj>() {
        @Override
        public ShipmentObj createFromParcel(Parcel in) {
            return new ShipmentObj(in);
        }

        @Override
        public ShipmentObj[] newArray(int size) {
            return new ShipmentObj[size];
        }
    };

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

    public List<CourierObj> getListCourier() {
        return listCourier;
    }

    public void setListCourier(List<CourierObj> listCourier) {
        this.listCourier = listCourier;
    }
}

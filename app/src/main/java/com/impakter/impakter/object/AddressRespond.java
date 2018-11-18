package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("checkAddNew")
    private boolean checkAddNew;
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

    public boolean getCheckAddNew() {
        return checkAddNew;
    }

    public void setCheckAddNew(boolean checkAddNew) {
        this.checkAddNew = checkAddNew;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("otherAddress")
        private OtherAddress otherAddress;
        @Expose
        @SerializedName("primaryAddress")
        private PrimaryAddress primaryAddress;

        public OtherAddress getOtherAddress() {
            return otherAddress;
        }

        public void setOtherAddress(OtherAddress otherAddress) {
            this.otherAddress = otherAddress;
        }

        public PrimaryAddress getPrimaryAddress() {
            return primaryAddress;
        }

        public void setPrimaryAddress(PrimaryAddress primaryAddress) {
            this.primaryAddress = primaryAddress;
        }
    }

    public static class OtherAddress implements Parcelable {
        @Expose
        @SerializedName("phoneNumber")
        private String phoneNumber;
        @Expose
        @SerializedName("zipcode")
        private String zipcode;
        @Expose
        @SerializedName("countryId")
        private int countryId;
        @Expose
        @SerializedName("country")
        private String country;
        @Expose
        @SerializedName("state")
        private String state;
        @Expose
        @SerializedName("cityTown")
        private String cityTown;
        @Expose
        @SerializedName("address")
        private String address;

        protected OtherAddress(Parcel in) {
            phoneNumber = in.readString();
            zipcode = in.readString();
            countryId = in.readInt();
            country = in.readString();
            state = in.readString();
            cityTown = in.readString();
            address = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(phoneNumber);
            dest.writeString(zipcode);
            dest.writeInt(countryId);
            dest.writeString(country);
            dest.writeString(state);
            dest.writeString(cityTown);
            dest.writeString(address);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OtherAddress> CREATOR = new Creator<OtherAddress>() {
            @Override
            public OtherAddress createFromParcel(Parcel in) {
                return new OtherAddress(in);
            }

            @Override
            public OtherAddress[] newArray(int size) {
                return new OtherAddress[size];
            }
        };

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public int getCountryId() {
            return countryId;
        }

        public void setCountryId(int countryId) {
            this.countryId = countryId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCityTown() {
            return cityTown;
        }

        public void setCityTown(String cityTown) {
            this.cityTown = cityTown;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class PrimaryAddress implements Parcelable {
        @Expose
        @SerializedName("phoneNumber")
        private String phoneNumber;
        @Expose
        @SerializedName("zipcode")
        private String zipcode;
        @Expose
        @SerializedName("countryId")
        private int countryId;
        @Expose
        @SerializedName("country")
        private String country;
        @Expose
        @SerializedName("state")
        private String state;
        @Expose
        @SerializedName("cityTown")
        private String cityTown;
        @Expose
        @SerializedName("address")
        private String address;

        protected PrimaryAddress(Parcel in) {
            phoneNumber = in.readString();
            zipcode = in.readString();
            countryId = in.readInt();
            country = in.readString();
            state = in.readString();
            cityTown = in.readString();
            address = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(phoneNumber);
            dest.writeString(zipcode);
            dest.writeInt(countryId);
            dest.writeString(country);
            dest.writeString(state);
            dest.writeString(cityTown);
            dest.writeString(address);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PrimaryAddress> CREATOR = new Creator<PrimaryAddress>() {
            @Override
            public PrimaryAddress createFromParcel(Parcel in) {
                return new PrimaryAddress(in);
            }

            @Override
            public PrimaryAddress[] newArray(int size) {
                return new PrimaryAddress[size];
            }
        };

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public int getCountryId() {
            return countryId;
        }

        public void setCountryId(int countryId) {
            this.countryId = countryId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCityTown() {
            return cityTown;
        }

        public void setCityTown(String cityTown) {
            this.cityTown = cityTown;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

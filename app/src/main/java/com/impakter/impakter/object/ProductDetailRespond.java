package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailRespond {


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
        @SerializedName("moreFromThisBrand")
        private List<MoreFromThisBrand> moreFromThisBrand;
        @Expose
        @SerializedName("introduction")
        private String introduction;
        @Expose
        @SerializedName("totalView")
        private int totalView;
        @Expose
        @SerializedName("totalFavorite")
        private int totalFavorite;
        @Expose
        @SerializedName("totalComment")
        private int totalComment;
        @Expose
        @SerializedName("totalUpvote")
        private int totalUpvote;
        @Expose
        @SerializedName("totalRating")
        private int totalRating;
        @Expose
        @SerializedName("averageRating")
        private float averageRating;
        @Expose
        @SerializedName("dateModified")
        private String dateModified;
        @Expose
        @SerializedName("dateCreated")
        private String dateCreated;
        @Expose
        @SerializedName("orderNumber")
        private int orderNumber;
        @Expose
        @SerializedName("shipCost")
        private int shipCost;
        @Expose
        @SerializedName("shipModel")
        private String shipModel;
        @Expose
        @SerializedName("shipCourierId")
        private String shipCourierId;
        @Expose
        @SerializedName("shipAvailability")
        private String shipAvailability;
        @Expose
        @SerializedName("shipWeightDetails")
        private String shipWeightDetails;
        @Expose
        @SerializedName("shipWeightUnit")
        private String shipWeightUnit;
        @Expose
        @SerializedName("shipPackageDimensions")
        private List<String> shipPackageDimensions;
        @Expose
        @SerializedName("shipDimensionsUnit")
        private String shipDimensionsUnit;
        @Expose
        @SerializedName("shipPackageType")
        private int shipPackageType;
        @Expose
        @SerializedName("shipHandlingTime")
        private String shipHandlingTime;
        @Expose
        @SerializedName("shipItemLocation")
        private String shipItemLocation;
        @Expose
        @SerializedName("priceDonation")
        private float priceDonation;
        @Expose
        @SerializedName("priceSaleTaxPercentage")
        private float priceSaleTaxPercentage;
        @Expose
        @SerializedName("priceSaleTax")
        private float priceSaleTax;
        @Expose
        @SerializedName("priceDiscountPercentage")
        private float priceDiscountPercentage;
        @Expose
        @SerializedName("priceDiscount")
        private float priceDiscount;
        @Expose
        @SerializedName("pricePrimary")
        private float pricePrimary;
        @Expose
        @SerializedName("priceCurrency")
        private float priceCurrency;
        @Expose
        @SerializedName("priceFormat")
        private float priceFormat;
        @Expose
        @SerializedName("countryOfManufacture")
        private String countryOfManufacture;
        @Expose
        @SerializedName("mpn")
        private String mpn;
        @Expose
        @SerializedName("upc")
        private String upc;
        @Expose
        @SerializedName("curatorName")
        private String curatorName;
        @Expose
        @SerializedName("brandName")
        private String brandName;
        @Expose
        @SerializedName("subGender")
        private String subGender;
        @Expose
        @SerializedName("subClass")
        private String subClass;
        @Expose
        @SerializedName("subCategory")
        private String subCategory;
        @Expose
        @SerializedName("category")
        private String category;
        @Expose
        @SerializedName("images")
        private List<String> images;
        @Expose
        @SerializedName("quantity")
        private int quantity;
        @Expose
        @SerializedName("optionAttribute")
        private OptionAttribute optionAttribute;
        @Expose
        @SerializedName("sustainabilityOptions")
        private String sustainabilityOptions;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("descriptionFeatures")
        private List<String> descriptionFeatures;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("title")
        private String title;
        @Expose
        @SerializedName("sellerId")
        private int sellerId;
        @Expose
        @SerializedName("id")
        private int id;

        public List<MoreFromThisBrand> getMoreFromThisBrand() {
            return moreFromThisBrand;
        }

        public void setMoreFromThisBrand(List<MoreFromThisBrand> moreFromThisBrand) {
            this.moreFromThisBrand = moreFromThisBrand;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getTotalView() {
            return totalView;
        }

        public void setTotalView(int totalView) {
            this.totalView = totalView;
        }

        public int getTotalFavorite() {
            return totalFavorite;
        }

        public void setTotalFavorite(int totalFavorite) {
            this.totalFavorite = totalFavorite;
        }

        public int getTotalComment() {
            return totalComment;
        }

        public void setTotalComment(int totalComment) {
            this.totalComment = totalComment;
        }

        public int getTotalUpvote() {
            return totalUpvote;
        }

        public void setTotalUpvote(int totalUpvote) {
            this.totalUpvote = totalUpvote;
        }

        public int getTotalRating() {
            return totalRating;
        }

        public void setTotalRating(int totalRating) {
            this.totalRating = totalRating;
        }

        public float getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(float averageRating) {
            this.averageRating = averageRating;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getShipCost() {
            return shipCost;
        }

        public void setShipCost(int shipCost) {
            this.shipCost = shipCost;
        }

        public String getShipModel() {
            return shipModel;
        }

        public void setShipModel(String shipModel) {
            this.shipModel = shipModel;
        }

        public String getShipCourierId() {
            return shipCourierId;
        }

        public void setShipCourierId(String shipCourierId) {
            this.shipCourierId = shipCourierId;
        }

        public String getShipAvailability() {
            return shipAvailability;
        }

        public void setShipAvailability(String shipAvailability) {
            this.shipAvailability = shipAvailability;
        }

        public String getShipWeightDetails() {
            return shipWeightDetails;
        }

        public void setShipWeightDetails(String shipWeightDetails) {
            this.shipWeightDetails = shipWeightDetails;
        }

        public String getShipWeightUnit() {
            return shipWeightUnit;
        }

        public void setShipWeightUnit(String shipWeightUnit) {
            this.shipWeightUnit = shipWeightUnit;
        }

        public List<String> getShipPackageDimensions() {
            return shipPackageDimensions;
        }

        public void setShipPackageDimensions(List<String> shipPackageDimensions) {
            this.shipPackageDimensions = shipPackageDimensions;
        }

        public String getShipDimensionsUnit() {
            return shipDimensionsUnit;
        }

        public void setShipDimensionsUnit(String shipDimensionsUnit) {
            this.shipDimensionsUnit = shipDimensionsUnit;
        }

        public int getShipPackageType() {
            return shipPackageType;
        }

        public void setShipPackageType(int shipPackageType) {
            this.shipPackageType = shipPackageType;
        }

        public String getShipHandlingTime() {
            return shipHandlingTime;
        }

        public void setShipHandlingTime(String shipHandlingTime) {
            this.shipHandlingTime = shipHandlingTime;
        }

        public String getShipItemLocation() {
            return shipItemLocation;
        }

        public void setShipItemLocation(String shipItemLocation) {
            this.shipItemLocation = shipItemLocation;
        }

        public float getPriceDonation() {
            return priceDonation;
        }

        public void setPriceDonation(float priceDonation) {
            this.priceDonation = priceDonation;
        }

        public float getPriceSaleTaxPercentage() {
            return priceSaleTaxPercentage;
        }

        public void setPriceSaleTaxPercentage(float priceSaleTaxPercentage) {
            this.priceSaleTaxPercentage = priceSaleTaxPercentage;
        }

        public float getPriceSaleTax() {
            return priceSaleTax;
        }

        public void setPriceSaleTax(float priceSaleTax) {
            this.priceSaleTax = priceSaleTax;
        }

        public float getPriceDiscountPercentage() {
            return priceDiscountPercentage;
        }

        public void setPriceDiscountPercentage(float priceDiscountPercentage) {
            this.priceDiscountPercentage = priceDiscountPercentage;
        }

        public float getPriceDiscount() {
            return priceDiscount;
        }

        public void setPriceDiscount(float priceDiscount) {
            this.priceDiscount = priceDiscount;
        }

        public float getPricePrimary() {
            return pricePrimary;
        }

        public void setPricePrimary(float pricePrimary) {
            this.pricePrimary = pricePrimary;
        }

        public float getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(int priceCurrency) {
            this.priceCurrency = priceCurrency;
        }

        public float getPriceFormat() {
            return priceFormat;
        }

        public void setPriceFormat(float priceFormat) {
            this.priceFormat = priceFormat;
        }

        public String getCountryOfManufacture() {
            return countryOfManufacture;
        }

        public void setCountryOfManufacture(String countryOfManufacture) {
            this.countryOfManufacture = countryOfManufacture;
        }

        public String getMpn() {
            return mpn;
        }

        public void setMpn(String mpn) {
            this.mpn = mpn;
        }

        public String getUpc() {
            return upc;
        }

        public void setUpc(String upc) {
            this.upc = upc;
        }

        public String getCuratorName() {
            return curatorName;
        }

        public void setCuratorName(String curatorName) {
            this.curatorName = curatorName;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getSubGender() {
            return subGender;
        }

        public void setSubGender(String subGender) {
            this.subGender = subGender;
        }

        public String getSubClass() {
            return subClass;
        }

        public void setSubClass(String subClass) {
            this.subClass = subClass;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public OptionAttribute getOptionAttribute() {
            return optionAttribute;
        }

        public void setOptionAttribute(OptionAttribute optionAttribute) {
            this.optionAttribute = optionAttribute;
        }

        public String getSustainabilityOptions() {
            return sustainabilityOptions;
        }

        public void setSustainabilityOptions(String sustainabilityOptions) {
            this.sustainabilityOptions = sustainabilityOptions;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getDescriptionFeatures() {
            return descriptionFeatures;
        }

        public void setDescriptionFeatures(List<String> descriptionFeatures) {
            this.descriptionFeatures = descriptionFeatures;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class MoreFromThisBrand implements Parcelable {
        @Expose
        @SerializedName("images")
        private String images;
        @Expose
        @SerializedName("title")
        private String title;
        @Expose
        @SerializedName("id")
        private int id;

        protected MoreFromThisBrand(Parcel in) {
            images = in.readString();
            title = in.readString();
            id = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(images);
            dest.writeString(title);
            dest.writeInt(id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MoreFromThisBrand> CREATOR = new Creator<MoreFromThisBrand>() {
            @Override
            public MoreFromThisBrand createFromParcel(Parcel in) {
                return new MoreFromThisBrand(in);
            }

            @Override
            public MoreFromThisBrand[] newArray(int size) {
                return new MoreFromThisBrand[size];
            }
        };

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    public static class OptionAttribute {
        @Expose
        @SerializedName("Occasion")
        private String Occasion;
        @Expose
        @SerializedName("Season")
        private String Season;
        @Expose
        @SerializedName("Quantity Details")
        private String quantityDetail;
        @Expose
        @SerializedName("Color")
        private List<String> Color;
        @Expose
        @SerializedName("Color Type")
        private String colorType;
        @Expose
        @SerializedName("Size")
        private List<String> Size;
        @Expose
        @SerializedName("Region Of Size")
        private String regionOfSize;
        @Expose
        @SerializedName("Size Type")
        private String sizeType;

        public String getOccasion() {
            return Occasion;
        }

        public void setOccasion(String Occasion) {
            this.Occasion = Occasion;
        }

        public String getSeason() {
            return Season;
        }

        public void setSeason(String Season) {
            this.Season = Season;
        }

        public List<String> getColor() {
            return Color;
        }

        public void setColor(List<String> Color) {
            this.Color = Color;
        }

        public List<String> getSize() {
            return Size;
        }

        public void setSize(List<String> Size) {
            this.Size = Size;
        }

        public String getQuantityDetail() {
            return quantityDetail;
        }

        public void setQuantityDetail(String quantityDetail) {
            this.quantityDetail = quantityDetail;
        }

        public String getColorType() {
            return colorType;
        }

        public void setColorType(String colorType) {
            this.colorType = colorType;
        }

        public String getRegionOfSize() {
            return regionOfSize;
        }

        public void setRegionOfSize(String regionOfSize) {
            this.regionOfSize = regionOfSize;
        }

        public String getSizeType() {
            return sizeType;
        }

        public void setSizeType(String sizeType) {
            this.sizeType = sizeType;
        }
    }
}

package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellerProfileRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("arrProduct")
    private List<ArrProduct> arrProduct;
    @Expose
    @SerializedName("sellerInfo")
    private SellerInfo sellerInfo;
    @Expose
    @SerializedName("allPage")
    private int allPage;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ArrProduct> getArrProduct() {
        return arrProduct;
    }

    public void setArrProduct(List<ArrProduct> arrProduct) {
        this.arrProduct = arrProduct;
    }

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ArrProduct {
        @Expose
        @SerializedName("totalRating")
        private int totalRating;
        @Expose
        @SerializedName("totalComment")
        private int totalComment;
        @Expose
        @SerializedName("totalFavori")
        private int totalFavori;
        @Expose
        @SerializedName("totalView")
        private int totalView;
        @Expose
        @SerializedName("averageRating")
        private float averageRating;
        @Expose
        @SerializedName("price")
        private float price;
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

        public int getTotalRating() {
            return totalRating;
        }

        public void setTotalRating(int totalRating) {
            this.totalRating = totalRating;
        }

        public int getTotalComment() {
            return totalComment;
        }

        public void setTotalComment(int totalComment) {
            this.totalComment = totalComment;
        }

        public int getTotalFavori() {
            return totalFavori;
        }

        public void setTotalFavori(int totalFavori) {
            this.totalFavori = totalFavori;
        }

        public int getTotalView() {
            return totalView;
        }

        public void setTotalView(int totalView) {
            this.totalView = totalView;
        }

        public float getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(float averageRating) {
            this.averageRating = averageRating;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
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

    public static class SellerInfo {
        @SerializedName("followStatus")
        private boolean followStatus;
        @Expose
        @SerializedName("introduction")
        private String introduction;
        @Expose
        @SerializedName("totalUserFollow")
        private String totalUserFollow;
        @Expose
        @SerializedName("countryName")
        private String countryName;
        @Expose
        @SerializedName("stateProvince")
        private String stateProvince;
        @Expose
        @SerializedName("cityTown")
        private String cityTown;
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("sellerCover")
        private String sellerCover;
        @Expose
        @SerializedName("sellerAvatar")
        private String sellerAvatar;
        @Expose
        @SerializedName("sellerName")
        private String sellerName;
        @Expose
        @SerializedName("id")
        private int id;

        public boolean isFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(boolean followStatus) {
            this.followStatus = followStatus;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getTotalUserFollow() {
            return totalUserFollow;
        }

        public void setTotalUserFollow(String totalUserFollow) {
            this.totalUserFollow = totalUserFollow;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getStateProvince() {
            return stateProvince;
        }

        public void setStateProvince(String stateProvince) {
            this.stateProvince = stateProvince;
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

        public String getSellerCover() {
            return sellerCover;
        }

        public void setSellerCover(String sellerCover) {
            this.sellerCover = sellerCover;
        }

        public String getSellerAvatar() {
            return sellerAvatar;
        }

        public void setSellerAvatar(String sellerAvatar) {
            this.sellerAvatar = sellerAvatar;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

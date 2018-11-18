package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserObj {
    @SerializedName("followStatus")
    private int followStatus;
    @SerializedName("numberOfCollection")
    private int numberOfCollection;
    @SerializedName("numberOfFollowers")
    private int numberOfFollowers;
    @SerializedName("numberOfFollowings")
    private int numberOfFollowings;
    @Expose
    @SerializedName("bankNumber")
    private String bankNumber;
    @Expose
    @SerializedName("bankRoutingNumber")
    private String bankRoutingNumber;
    @Expose
    @SerializedName("bankHolderName")
    private String bankHolderName;
    @Expose
    @SerializedName("bankLocationId")
    private String bankLocationId;
    @Expose
    @SerializedName("paypalEmail")
    private String paypalEmail;
    @Expose
    @SerializedName("cardExpirationDate")
    private String cardExpirationDate;
    @Expose
    @SerializedName("cardNumberCvv")
    private String cardNumberCvv;
    @SerializedName("cardNumberToDisplay")
    private String cardNumberToDisplay;
    @Expose
    @SerializedName("cardNumber")
    private String cardNumber;
    @Expose
    @SerializedName("cardHolderName")
    private String cardHolderName;
    @SerializedName("socialNetworkId")
    private String socialNetworkId;
    @Expose
    @SerializedName("accountType")
    private String accountType;
    @Expose
    @SerializedName("loginTime")
    private String loginTime;
    @Expose
    @SerializedName("loginToken")
    private String loginToken;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("note")
    private String note;
    @Expose
    @SerializedName("cover")
    private String cover;
    @Expose
    @SerializedName("avatar")
    private String avatar;
    @Expose
    @SerializedName("introduction")
    private String introduction;
    @Expose
    @SerializedName("countryId")
    private String countryId;
    @Expose
    @SerializedName("stateProvince")
    private String stateProvince;
    @Expose
    @SerializedName("cityTown")
    private String cityTown;
    @Expose
    @SerializedName("postalCode")
    private String postalCode;
    @Expose
    @SerializedName("address2")
    private String address2;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("lastName")
    private String lastName;
    @Expose
    @SerializedName("firstName")
    private String firstName;
    @Expose
    @SerializedName("role")
    private int role;
    @Expose
    @SerializedName("updatedAt")
    private String updatedAt;
    @Expose
    @SerializedName("createdAt")
    private String createdAt;
    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("passwordResetToken")
    private String passwordResetToken;
    @Expose
    @SerializedName("passwordHash")
    private String passwordHash;
    @Expose
    @SerializedName("authKey")
    private String authKey;
    @Expose
    @SerializedName("username")
    private String username;
    @Expose
    @SerializedName("id")
    private int id;

    public String getSocialNetworkId() {
        return socialNetworkId;
    }

    public void setSocialNetworkId(String socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }

    public String getCardNumberToDisplay() {
        return cardNumberToDisplay;
    }

    public void setCardNumberToDisplay(String cardNumberToDisplay) {
        this.cardNumberToDisplay = cardNumberToDisplay;
    }

    private String password;

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankRoutingNumber() {
        return bankRoutingNumber;
    }

    public void setBankRoutingNumber(String bankRoutingNumber) {
        this.bankRoutingNumber = bankRoutingNumber;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public void setBankHolderName(String bankHolderName) {
        this.bankHolderName = bankHolderName;
    }

    public String getBankLocationId() {
        return bankLocationId;
    }

    public void setBankLocationId(String bankLocationId) {
        this.bankLocationId = bankLocationId;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(String cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public String getCardNumberCvv() {
        return cardNumberCvv;
    }

    public void setCardNumberCvv(String cardNumberCvv) {
        this.cardNumberCvv = cardNumberCvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfCollection() {
        return numberOfCollection;
    }

    public void setNumberOfCollection(int numberOfCollection) {
        this.numberOfCollection = numberOfCollection;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public int getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public void setNumberOfFollowings(int numberOfFollowings) {
        this.numberOfFollowings = numberOfFollowings;
    }
}

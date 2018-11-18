package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandObj {

    @Expose
    @SerializedName("typeBrand")
    private String typeBrand;
    @Expose
    @SerializedName("avatar")
    private String avatar;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getTypeBrand() {
        return typeBrand;
    }

    public void setTypeBrand(String typeBrand) {
        this.typeBrand = typeBrand;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

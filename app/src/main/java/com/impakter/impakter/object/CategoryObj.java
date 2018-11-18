package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryObj {
    public CategoryObj(String catName, int catId) {
        this.catName = catName;
        this.catId = catId;
    }

    @Expose
    @SerializedName("catName")
    private String catName;
    @Expose
    @SerializedName("catId")
    private int catId;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }
}

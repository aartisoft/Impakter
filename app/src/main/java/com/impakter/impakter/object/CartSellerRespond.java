package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartSellerRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("listCategory")
    private List<CategoryObj> listCategory;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryObj> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<CategoryObj> listCategory) {
        this.listCategory = listCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public static class ListCategory {
//        @Expose
//        @SerializedName("catName")
//        private String catName;
//        @Expose
//        @SerializedName("catId")
//        private int catId;
//
//        public String getCatName() {
//            return catName;
//        }
//
//        public void setCatName(String catName) {
//            this.catName = catName;
//        }
//
//        public int getCatId() {
//            return catId;
//        }
//
//        public void setCatId(int catId) {
//            this.catId = catId;
//        }
//    }
}

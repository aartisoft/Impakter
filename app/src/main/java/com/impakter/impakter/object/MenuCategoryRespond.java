package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuCategoryRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<CategoryObj> data;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryObj> getData() {
        return data;
    }

    public void setData(List<CategoryObj> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public static class Data {
//        @Expose
//        @SerializedName("catName")
//        private String catName;
//        @Expose
//        @SerializedName("catId")
//        private String catId;
//
//        public String getCatName() {
//            return catName;
//        }
//
//        public void setCatName(String catName) {
//            this.catName = catName;
//        }
//
//        public String getCatId() {
//            return catId;
//        }
//
//        public void setCatId(String catId) {
//            this.catId = catId;
//        }
//    }
}

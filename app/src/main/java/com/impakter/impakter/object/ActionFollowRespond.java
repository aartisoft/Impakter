package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionFollowRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("followStatus")
    private boolean followStatus;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(boolean followStatus) {
        this.followStatus = followStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

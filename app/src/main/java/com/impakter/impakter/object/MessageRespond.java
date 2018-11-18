package com.impakter.impakter.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageRespond {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<MessageObj> data;
    @Expose
    @SerializedName("allPages")
    private int allPages;
    @Expose
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MessageObj> getData() {
        return data;
    }

    public void setData(List<MessageObj> data) {
        this.data = data;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("fileAttach")
        private FileAttach fileAttach;
        @Expose
        @SerializedName("content")
        private String content;
        @Expose
        @SerializedName("time")
        private int time;
        @Expose
        @SerializedName("receiverAvatar")
        private String receiverAvatar;
        @Expose
        @SerializedName("senderAvatar")
        private String senderAvatar;
        @Expose
        @SerializedName("receiverId")
        private int receiverId;
        @Expose
        @SerializedName("senderId")
        private int senderId;
        @Expose
        @SerializedName("messId")
        private int messId;

        public FileAttach getFileAttach() {
            return fileAttach;
        }

        public void setFileAttach(FileAttach fileAttach) {
            this.fileAttach = fileAttach;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getReceiverAvatar() {
            return receiverAvatar;
        }

        public void setReceiverAvatar(String receiverAvatar) {
            this.receiverAvatar = receiverAvatar;
        }

        public String getSenderAvatar() {
            return senderAvatar;
        }

        public void setSenderAvatar(String senderAvatar) {
            this.senderAvatar = senderAvatar;
        }

        public int getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(int receiverId) {
            this.receiverId = receiverId;
        }

        public int getSenderId() {
            return senderId;
        }

        public void setSenderId(int senderId) {
            this.senderId = senderId;
        }

        public int getMessId() {
            return messId;
        }

        public void setMessId(int messId) {
            this.messId = messId;
        }
    }

    public static class FileAttach {
        @Expose
        @SerializedName("height")
        private int height;
        @Expose
        @SerializedName("width")
        private int width;
        @Expose
        @SerializedName("image")
        private String image;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

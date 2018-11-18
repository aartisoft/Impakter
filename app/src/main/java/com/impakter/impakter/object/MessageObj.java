package com.impakter.impakter.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageObj implements Parcelable {

    @Expose
    @SerializedName("fileAttach")
    private FileAttach fileAttach;
    @Expose
    @SerializedName("content")
    private String content;
    @Expose
    @SerializedName("time")
    private long time;
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

    protected MessageObj(Parcel in) {
        content = in.readString();
        time = in.readLong();
        receiverAvatar = in.readString();
        senderAvatar = in.readString();
        receiverId = in.readInt();
        senderId = in.readInt();
        messId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeLong(time);
        dest.writeString(receiverAvatar);
        dest.writeString(senderAvatar);
        dest.writeInt(receiverId);
        dest.writeInt(senderId);
        dest.writeInt(messId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageObj> CREATOR = new Creator<MessageObj>() {
        @Override
        public MessageObj createFromParcel(Parcel in) {
            return new MessageObj(in);
        }

        @Override
        public MessageObj[] newArray(int size) {
            return new MessageObj[size];
        }
    };

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    public static class FileAttach implements Parcelable {
        @Expose
        @SerializedName("height")
        private int height;
        @Expose
        @SerializedName("width")
        private int width;
        @Expose
        @SerializedName("image")
        private String image;

        protected FileAttach(Parcel in) {
            height = in.readInt();
            width = in.readInt();
            image = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(height);
            dest.writeInt(width);
            dest.writeString(image);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<FileAttach> CREATOR = new Creator<FileAttach>() {
            @Override
            public FileAttach createFromParcel(Parcel in) {
                return new FileAttach(in);
            }

            @Override
            public FileAttach[] newArray(int size) {
                return new FileAttach[size];
            }
        };

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

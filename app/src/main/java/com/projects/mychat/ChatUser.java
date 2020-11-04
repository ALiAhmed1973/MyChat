package com.projects.mychat;

import android.os.Parcel;
import android.os.Parcelable;

import sdk.chat.core.dao.User;

public class ChatUser implements Parcelable {

    private String name;
    private String email;
    private String avatarURL;
    private String entityID;
    private Long id;


    public ChatUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatarURL = user.getAvatarURL();
        this.entityID= user.getEntityID();
        this.id = user.getId();
    }

    protected ChatUser(Parcel in) {
        name = in.readString();
        email = in.readString();
        avatarURL = in.readString();
        entityID = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(avatarURL);
        dest.writeString(entityID);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatUser> CREATOR = new Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel in) {
            return new ChatUser(in);
        }

        @Override
        public ChatUser[] newArray(int size) {
            return new ChatUser[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getEntityID() {
        return entityID;
    }

    public Long getId() {
        return id;
    }
}

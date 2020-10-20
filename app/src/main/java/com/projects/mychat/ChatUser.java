package com.projects.mychat;

import android.os.Parcel;
import android.os.Parcelable;

import sdk.chat.core.dao.User;

public class ChatUser implements Parcelable {
    private User user;

    public ChatUser(User user) {
        this.user = user;
    }

    protected ChatUser(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) user,flags);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

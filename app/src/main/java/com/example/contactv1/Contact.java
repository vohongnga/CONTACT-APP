package com.example.contactv1;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Contact implements Serializable {
    private String id;
    private String mName;
    private String mMobile;
    private byte[] mAvatar;


    public Contact(String mName, String mMobile, byte[] mAvatar) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.mName = mName;
        this.mMobile = mMobile;
        this.mAvatar = mAvatar;
    }


    public Contact(String id, String mName, String mMobile, byte[] mAvatar) {
        this.id = id;
        this.mName = mName;
        this.mMobile = mMobile;
        this.mAvatar = mAvatar;
    }

    public String getId() {
        return id;
    }

    public String getmName() {
        return mName;
    }

    public String getmMobile() {
        return mMobile;
    }

    public byte[] getmAvatar() {
        return mAvatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public void setmAvatar(byte[] mAvatar) {
        this.mAvatar = mAvatar;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ten: " + getmName() + "\tSdt: " + getmMobile();
    }
}

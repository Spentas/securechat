package com.spentas.javad.securechat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by javad on 11/12/2015.
 */
public class User  {
    private String image;
    private String username;
    private String publickey;

    @Override
    public String toString() {
        return "User [username=" + username + ", publickey=" + publickey +", image"+image+ "]";
    }



    public User(String image, String username, String publickey) {
        this.image = image;
        this.username = username;
        this.publickey = publickey;
    }

    public User() {
        // TODO Auto-generated constructor stub
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

package com.spentas.javad.securechat.model;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by javad on 11/12/2015.
 */
public class User  {
    private String image;
    private String username;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    @Override
    public String toString() {
        return "User [username=" + username + ", publicKey=" + publicKey +", image"+image+ "]";
    }



    public User(String image, String username, PublicKey publickey, PrivateKey privateKey) {
        this.image = image;
        this.username = username;
        this.publicKey = publickey;
        this.privateKey= privateKey;
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
    
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}

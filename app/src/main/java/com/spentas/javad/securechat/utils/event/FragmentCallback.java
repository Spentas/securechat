package com.spentas.javad.securechat.utils.event;

import com.spentas.javad.securechat.model.User;

import java.net.UnknownServiceException;

/**
 * Created by javad on 11/18/2015.
 */
public class FragmentCallback {
    private User user;
   public FragmentCallback(User user){
       this.user = user;
    }
    public User getUser(){
        return user;
    }

}

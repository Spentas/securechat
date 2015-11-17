package com.spentas.javad.securechat.utils;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by javad on 11/9/2015.
 */

public interface Callback {
    public void httpCallback(JSONObject object);
    /**
     *
     * @return corresponding context
     */
    public Context getContext();


    public enum CallbackType{
        Fragment_Callback,HTTP_CALLBACK
    }
}


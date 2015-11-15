package com.spentas.javad.securechat.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by javad on 11/6/2015.
 */
public class SharedPreference {


    private static final String KEY_SHARED_PREF = "SECURE_CHAT";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_SESSION_ID = "sessionId",
            FLAG_MESSAGE = "message", KEY_TOKEN = "token", KEY_USERNAME = "username", FLAG_LOGIN = "login";
    private Context context;
    private SharedPreferences sharedPref;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public void storeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FLAG_LOGIN, status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        return sharedPref.getBoolean(FLAG_LOGIN, false);
    }

    public void storeWsSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getWsSessionId() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public void storeLoginInfo(String username, String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TOKEN, password);
        editor.commit();
    }

    public String getToken() {
        return sharedPref.getString(KEY_TOKEN, null);
    }


}

package com.spentas.javad.securechat.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javad on 11/6/2015.
 */
public class SharedPreference {


        private Context context;
        private SharedPreferences sharedPref;

        private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
        private static final int KEY_MODE_PRIVATE = 0;
        private static final String KEY_SESSION_ID = "sessionId",
                FLAG_MESSAGE = "message";

        public SharedPreference(Context context) {
            this.context = context;
            sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
                    KEY_MODE_PRIVATE);
        }

        public void storeSessionId(String sessionId) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(KEY_SESSION_ID, sessionId);
            editor.commit();
        }

        public String getSessionId() {
            return sharedPref.getString(KEY_SESSION_ID, null);
        }

        public String getSendMessageJSON(String message) {
            String json = null;

            try {
                JSONObject jObj = new JSONObject();
                jObj.put("flag", FLAG_MESSAGE);
                jObj.put("sessionId", getSessionId());
                jObj.put("message", message);

                json = jObj.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }


    }

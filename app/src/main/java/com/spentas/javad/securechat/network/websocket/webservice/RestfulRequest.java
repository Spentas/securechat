package com.spentas.javad.securechat.network.websocket.webservice;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.spentas.javad.securechat.LoginActivity;
import com.spentas.javad.securechat.RegistrationActivity;
import com.spentas.javad.securechat.network.websocket.NetworkConfig;
import com.spentas.javad.securechat.utils.Callback;
import com.spentas.javad.securechat.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by javad on 11/8/2015.
 */
public class RestfulRequest {
    private static boolean authenticated = false;
    private static Context context;
    public static void login(RequestParams params, final Callback callback) {
        context = callback.getContext();
        Utils.progressDialog(context).show();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(NetworkConfig.BASE + NetworkConfig.REST_PORT + NetworkConfig.REST_LOGIN_ENDPOINT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
               Utils.progressDialog(context).hide();

            }


            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, String s) {
                Utils.progressDialog(context).hide();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("status")) {
                        System.out.println("login");
                        callback.internalNotification();
                    } else {
                        System.out.println("failed to login1");
                        Utils.progressDialog(context).hide();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.progressDialog(context).hide();


            }
        });

    }

    public static void register(RequestParams params, final Context context) {
        final com.spentas.javad.securechat.utils.Callback callback = (RegistrationActivity) context;
        Utils.progressDialog(context).show();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(NetworkConfig.BASE + NetworkConfig.REST_PORT + NetworkConfig.REST_REGISTER_ENDPOINT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                Utils.progressDialog(context).dismiss();
                throwable.printStackTrace();
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("status")) {
                        System.out.println("register");
                        callback.internalNotification();
                    } else
                        System.out.println("failed to login");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.progressDialog(context).dismiss();
            }
        });

    }


}

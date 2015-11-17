package com.spentas.javad.securechat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.spentas.javad.securechat.utils.Log;
import com.spentas.javad.securechat.utils.Utils;

/**
 * Created by javad on 11/14/2015.
 */
public class ConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = "CONNECTION_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        boolean isConnected = networkInfo != null
                && networkInfo.isConnectedOrConnecting();

        boolean isConnected_by_wifi = networkInfoWifi != null
                && networkInfoWifi.isConnectedOrConnecting();
        Utils.setConnectionStatus(isConnected);
        Log.i((isConnected || isConnected_by_wifi) ? String.format("Connected by using 3G=%b || wifi=%b ", isConnected, isConnected_by_wifi) : "Disconnected");
    }
}

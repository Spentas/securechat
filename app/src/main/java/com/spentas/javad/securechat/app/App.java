package com.spentas.javad.securechat.app;

import android.app.Application;
import android.content.Context;

import com.spentas.javad.securechat.network.websocket.Connection;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;

/**
 * Created by javad on 10/29/2015.
 */
public class App extends Application {
    private Connection connection;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }
    public static Context getmContext(){
        return mContext;
    }
    public Connection getConnection(){
        return ConnectionManager.getConnection(ConnectionManager.ConnectionType.WEBSOCKET);
    }
}

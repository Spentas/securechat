package com.spentas.javad.securechat.network.websocket;

import android.app.Application;

import com.spentas.javad.securechat.app.App;

import javax.inject.Inject;

/**
 * Created by javad on 11/5/2015.
 */

public class ConnectionManager {
    @Inject
   volatile WsConnection wsConnection;


    public ConnectionManager(){
        ((App) App.getContext()).getComponent().inject(this);
    }

    public Connection getConnection(ConnectionType type) {
        System.out.println("connection manager " + wsConnection);
        switch (type) {
            case WEBSOCKET:
                if (wsConnection == null){
                    synchronized (WsConnection.class){
                        wsConnection = new WsConnection();
                    }
                }
                return wsConnection;
        }
        return null;
    }

    public enum ConnectionType {
        WEBSOCKET, RESTFUL, SOCKETIO
    }
}


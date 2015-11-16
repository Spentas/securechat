package com.spentas.javad.securechat.network.websocket;

/**
 * Created by javad on 11/5/2015.
 */
public interface Connection {
     boolean isConnected();
     String getReference();
    public void sendMessageToServer(String message);
    boolean connect();
    boolean disConnect();

}

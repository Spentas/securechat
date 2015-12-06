package com.spentas.javad.securechat.network.websocket;

import com.spentas.javad.securechat.model.Message;

/**
 * Created by javad on 11/5/2015.
 */
public interface Connection {
    boolean isConnected();
    String getInstance();
    public void sendMessageToServer(Message message);
    boolean connect();
    boolean disConnect();

}

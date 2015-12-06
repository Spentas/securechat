package com.spentas.javad.securechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.crypto.Util;
import com.spentas.javad.securechat.model.Message;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.network.websocket.Connection;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.network.websocket.WebSocketClient;
import com.spentas.javad.securechat.network.websocket.WsConnection;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.utils.MainThreadBus;
import com.spentas.javad.securechat.utils.event.DataSetChangeEvent;
import com.spentas.javad.securechat.utils.event.NewMessageEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import org.json.JSONObject;

import java.security.PublicKey;

import javax.inject.Inject;

/**
 * Created by javad on 11/20/2015.
 */
public class WsService extends Service implements WebSocketClient.Listener {

    private static final String TAG="wsservice";

    @Inject
    DbHelper db;

    @Inject
    Bus mBus;
    private Connection mConnection;
    private MainThreadBus mTbus;
    public WsService() {
        super();
    }
    private static final String TAG_MESSEGE="message",TAG_KEY="skey",TAG_FRIEND_REQ="friendreq",TAG_SELF="self";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Started");
        ((App)App.getContext()).getComponent().inject(this);
//        mBus.register(this);
        mTbus = MainThreadBus.getInstance();
        mTbus.register(this);
        mConnection = ConnectionManager.getConnection(ConnectionManager.ConnectionType.WEBSOCKET);
        ((WsConnection) mConnection).setListener(this);
//        mConnection.disConnect();
  //      mConnection.connect();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");

        mTbus.unregister(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onConnect() {
        Log.i(TAG,"Service ws connected");
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG,"Service ws got message" + message);

        try {
            JSONObject jsonObject = new JSONObject(message);
            String flag = jsonObject.getString("flag");
            if(flag.equalsIgnoreCase(TAG_SELF))
                return;

            ObjectMapper objectMapper = new ObjectMapper();
            Message msg =  objectMapper.readValue(message, Message.class);

            if (flag.equalsIgnoreCase(TAG_MESSEGE)){

                db.addMessage(msg, String.format("%s:%s", msg.getTo(), msg.getFrom()));
                mTbus.post(new NewMessageEvent(msg));

            }
            if (flag.equalsIgnoreCase(TAG_KEY)){
            User newFriend = new User();
                newFriend.setPublicKey(msg.getPublicKey());
                newFriend.setUsername(msg.getFrom());
                db.addFriend(newFriend, msg.getMessage());
                mTbus.post(new DataSetChangeEvent());
                Log.i(TAG, "new semmetric key was added" + message);
            }
            if (flag.equalsIgnoreCase(TAG_FRIEND_REQ)){
                User user = objectMapper.readValue(message, User.class);
                db.addFriend(user);
                Log.i(TAG, "new friend was added" + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onMessage(byte[] data) {

    }

    @Override
    public void onDisconnect(int code, String reason) {
        Log.i(TAG,"Service ws disconnected");

    }

    @Override
    public void onError(Exception error) {
        Log.i(TAG,"Service ws error" + error);
        error.printStackTrace();

    }


}

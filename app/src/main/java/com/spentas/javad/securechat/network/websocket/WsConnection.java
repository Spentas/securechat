package com.spentas.javad.securechat.network.websocket;


import android.app.Application;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Locale;

import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.network.NetworkConfig;
import com.spentas.javad.securechat.sqlite.SharedPreference;

import javax.inject.Inject;

/**
 * Created by javad on 11/5/2015.
 */
public class WsConnection implements Connection {


    // LogCat tag
    private static final String TAG = "WSCONNECTION";

    private WebSocketClient client;
    // will be injected
    @Inject
    SharedPreference sharedPreference;
    // Client name
    private String name = null;
    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";


    public WsConnection() {
        ((App) App.getContext()).getComponent().inject(this);
        init();
    }


    public void init(){

        final URI uri = URI.create(NetworkConfig.WS_URL
                + URLEncoder.encode(sharedPreference.getUserInfo().get("username")));
        Log.i(TAG, String.format("%s init object ref %s", uri.toString() , this.getReference()));
        client = new WebSocketClient(URI.create( NetworkConfig.WS_URL
                + URLEncoder.encode(sharedPreference.getUserInfo().get("username"))), new WebSocketClient.Listener() {

            @Override
            public void onConnect() {
                Log.i(TAG, String.format("%s new connection was establishied. object ref %s", uri.toString()));

            }

            /**
             * On receiving the message from web socket server
             * */
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);

            }

            @Override
            public void onMessage(byte[] data) {
                Log.d(TAG, String.format("Got binary message! %s",
                        bytesToHex(data)));

                // Message will be in JSON format
                parseMessage(bytesToHex(data));
            }

            /**
             * Called when the connection is terminated
             * */
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);
                Log.e(TAG, message);


                // clear the session id from shared preferences
                sharedPreference.storeWsSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

            }

        }, null);
        client.connect();
        System.out.println("client " + client);
    }

    /**
     * Method to send message to web socket server
     */
    @Override
    public void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            Log.e(TAG,String.format( "%s was sent to the server",message));

            client.send(message);
        }
    }

    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                sharedPreference.storeWsSessionId(sessionId);

                Log.e(TAG, "Your session id: " + sharedPreference.getWsSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");


            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(sharedPreference.getWsSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

              //  Message m = new Message(fromName, message, isSelf);


            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public String getReference() {
        return client.toString();
    }

    @Override
    public boolean connect() {
        if (client != null & !client.isConnected()) {
            client.connect();
            return true;
        }
        return false;
    }

    @Override
    public boolean disConnect() {
        if (client != null & client.isConnected()) {
            client.disconnect();
            return true;
        }
        return false;
    }
}



package com.spentas.javad.securechat.network.websocket;

/**
 * Created by javad on 11/5/2015.
 */

//@Module (injects =  {
//        Connection.class
//        })
public class ConnectionManager {
    private static volatile WsConnection wsConnection = null;
//public ConnectionManager(WsConnection connection){
//    this.wsConnection =connection;
//}
//
//    @Provides @Singleton
//    public  Connection getConnection(String type){
//            return type.equalsIgnoreCase("ws") ? wsConnection: null ;
//    }

    public static Connection getConnection(ConnectionType type) {
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


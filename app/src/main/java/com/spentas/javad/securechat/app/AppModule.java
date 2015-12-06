package com.spentas.javad.securechat.app;

import android.app.Application;

import com.spentas.javad.securechat.crypto.CryptoEngineFactory;
import com.spentas.javad.securechat.network.websocket.WsConnection;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javad on 11/14/2015.
 */
@Module
public class AppModule {


    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides @Singleton
    public Application application() {
        return app;
    }


    @Provides
    @Singleton
    Bus provideBus(){
        return new Bus();
    }

    @Provides
    @Singleton
    WsConnection provideConnection(){
    return new WsConnection();
}

    @Provides
    @Singleton
    SharedPreference provideSharedPreference(Application ctx){
        return new SharedPreference(ctx);
    }
    @Provides
    @Singleton
    DbHelper provideDbHelper(Application ctx){
        return new DbHelper(ctx);
    }

    @Provides
    CryptoEngineFactory provideCryptoEnginFactory(){return new CryptoEngineFactory();}
//    @Provides
//    ConnectionManager provideConnectionManager(){return new ConnectionManager();}

}

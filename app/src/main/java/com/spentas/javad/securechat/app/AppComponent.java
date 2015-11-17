package com.spentas.javad.securechat.app;

import com.spentas.javad.securechat.LoginActivity;
import com.spentas.javad.securechat.MainActivity;
import com.spentas.javad.securechat.SplashActivity;
import com.spentas.javad.securechat.adapter.SearchListAdapter;
import com.spentas.javad.securechat.fragment.FindFriendFragment;
import com.spentas.javad.securechat.fragment.FriendListFragment;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.network.websocket.WsConnection;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javad on 11/14/2015.
 */


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App application);
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(SplashActivity activity);
    void inject(FindFriendFragment findFriendFragment);
    void inject(WsConnection wsConnection);
    void inject(ConnectionManager connectionManager);
    void inject(FriendListFragment friendListFragment);
    void inject (SearchListAdapter searchListAdapter);

}

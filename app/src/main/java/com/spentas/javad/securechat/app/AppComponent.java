package com.spentas.javad.securechat.app;

import com.spentas.javad.securechat.LoginActivity;
import com.spentas.javad.securechat.MainActivity;
import com.spentas.javad.securechat.RegistrationActivity;
import com.spentas.javad.securechat.SplashActivity;
import com.spentas.javad.securechat.adapter.FriendListAdapter;
import com.spentas.javad.securechat.adapter.SearchListAdapter;
import com.spentas.javad.securechat.crypto.CryptoEngineFactory;
import com.spentas.javad.securechat.fragment.ConversationFragment;
import com.spentas.javad.securechat.fragment.FindFriendFragment;
import com.spentas.javad.securechat.fragment.FriendListFragment;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.network.websocket.WsConnection;
import com.spentas.javad.securechat.service.ConnectionReceiver;
import com.spentas.javad.securechat.service.WsService;

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
    void inject(FriendListAdapter friendListAdapter);
    void inject(ConversationFragment conversationFragment);
    void inject(WsService wsService);
    void inject(ConnectionReceiver connectionReceiver);
    void inject (RegistrationActivity registrationActivity);
    void inject (CryptoEngineFactory cryptoEngineFactory);
}

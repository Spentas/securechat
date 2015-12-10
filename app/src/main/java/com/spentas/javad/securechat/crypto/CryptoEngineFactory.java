package com.spentas.javad.securechat.crypto;

import com.spentas.javad.securechat.app.App;

/**
 * Created by javad on 12/6/2015.
 */
public class CryptoEngineFactory {

    public CryptoEngineFactory(){
        ((App) App.getContext()).getComponent().inject(this);
    }
    public CryptoEngine getEngine(KeyType type){
        switch (type){
            case AES128:
                return new AESEngine(128);
            case AES192:
                return new AESEngine(192);
            case AES256:
                return new AESEngine(256);
            case BLOWFISH:
                return new BlowFishEngine();
            case RSA: return new RSAEngine();
        }
        return null;
    }
}

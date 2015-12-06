package com.spentas.javad.securechat.crypto;

import org.spongycastle.jcajce.provider.symmetric.AES;

import java.security.Key;
import java.security.Security;

/**
 * Created by javad on 12/6/2015.
 */
public class AESEngine implements CryptoEngine {
    private static final String TAG = AESEngine.class.getSimpleName();
    private  int keySize;
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public AESEngine (int keySize){
        this.keySize = keySize;
    }

    @Override
    public Key keyGenerator() {
        return null;
    }

    @Override
    public String encrypt(String plainText) {
        return null;
    }

    @Override
    public String decrypt(String plainText) {
        return null;
    }
}

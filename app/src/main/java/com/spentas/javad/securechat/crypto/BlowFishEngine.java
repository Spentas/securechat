package com.spentas.javad.securechat.crypto;

import java.security.Key;

/**
 * Created by javad on 12/6/2015.
 */
public class BlowFishEngine implements CryptoEngine {
    @Override
    public Key keyGenerator() {
        return null;
    }

    @Override
    public String encrypt(String plainText, Key secretKey) {
        return null;
    }

    @Override
    public String decrypt(String cypherText, Key secretKey) {
        return null;
    }
}

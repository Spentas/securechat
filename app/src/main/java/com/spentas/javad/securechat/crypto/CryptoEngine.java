package com.spentas.javad.securechat.crypto;

import java.security.Key;

/**
 * Created by javad on 12/6/2015.
 */
public interface CryptoEngine {
    /**
     * @return
     */
    public Key keyGenerator();

    /**
     * @param plainText
     * @return
     */
    public String encrypt(String plainText);

    /**
     * @param cypherText
     * @return
     */
    public String decrypt(String cypherText);

}

package com.spentas.javad.securechat.crypto;

import android.util.Base64;

import org.spongycastle.jcajce.provider.symmetric.AES;
import org.spongycastle.util.encoders.Base64Encoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import cz.msebera.android.httpclient.impl.conn.tsccm.ConnPoolByRoute;

/**
 * Created by javad on 12/6/2015.
 */
public class AESEngine implements CryptoEngine {
    private static final String TAG = AESEngine.class.getSimpleName();
    private  int keySize;
    private static Cipher cipher;
    private static byte[] ivBytes = new byte[]{
            0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};
    IvParameterSpec ivParameterSpec;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public AESEngine (int keySize){
        this.keySize = keySize;
        ivParameterSpec = new IvParameterSpec(ivBytes);
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Key keyGenerator() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        keyGenerator.init(keySize);
        Key secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    @Override
    public String encrypt(String plainText,Key secretKey) {
        byte[] plainTextBytes = plainText.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);
            byte[] cypheredBytes = cipher.doFinal(plainTextBytes);

            return Util.encodeToBase64(cypheredBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String decrypt(String cipheredText,Key secretKey) {


        byte[] cipheredBytes = Base64.decode(cipheredText.getBytes(), Base64.DEFAULT);
        try {
            cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);
            byte[] plainBytes = cipher.doFinal(cipheredBytes);

            return new String(plainBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}

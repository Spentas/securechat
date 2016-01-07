package com.spentas.javad.securechat.crypto;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * Created by javad on 11/24/2015.
 */
public class Util {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }
    public static String encodePublicKey(PublicKey pbk) {
        byte[] base64Encoded = Base64.encode(pbk.getEncoded(), Base64.DEFAULT);
        String encoded = new String(base64Encoded);
        encoded = "-----BEGIN PUBLIC KEY-----\n" + encoded + "-----END PUBLIC KEY-----";
        return encoded;
    }

    public static String encodePrivateKey(PrivateKey prk) {
        byte[] base64Encoded = Base64.encode(prk.getEncoded(), Base64.DEFAULT);
        String encoded = new String(base64Encoded);
        encoded = "-----BEGIN PRIVATE KEY-----\n" + encoded + "-----END PRIVATE KEY-----";
        return encoded;
    }

    public static String encodeToBase64(byte[] cyphredBytes) {

        return Base64.encodeToString(cyphredBytes, Base64.DEFAULT);
    }

    public static byte[] decodeFromBase64(String cypheredText){
        return Base64.decode(cypheredText, Base64.DEFAULT);
    }


   public static PublicKey decodeRSAPublicFromBtyes(byte[] key){
       try {
           PublicKey publicKey = KeyFactory.getInstance("RSA","SC").generatePublic(new X509EncodedKeySpec(key));
           return publicKey;

       } catch (InvalidKeySpecException e) {
           e.printStackTrace();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       } catch (NoSuchProviderException e) {
           e.printStackTrace();
       }
       return null;
   }

//    public static void writePublicKeyToPreferences(KeyPair key) {
//        StringWriter publicStringWriter = new StringWriter();
//        try {
//            PemWriter pemWriter = new PemWriter(publicStringWriter);
//            pemWriter.writeObject(new PemObject("PUBLIC KEY", key.getPublic().getEncoded()));
//            pemWriter.flush();
//            pemWriter.close();
//            Preferences.putString(Preferences.RSA_PUBLIC_KEY, publicStringWriter.toString());
//        } catch (IOException e) {
//            Log.e("RSAEngine", e.getMessage());
//            e.printStackTrace();
//        }
//    }

//    public static void writePrivateKeyToPreferences(KeyPair keyPair) {
//        StringWriter privateStringWriter = new StringWriter();
//        try {
//            PemWriter pemWriter = new PemWriter(privateStringWriter);
//            pemWriter.writeObject(new PemObject("PRIVATE KEY", keyPair.getPrivate().getEncoded()));
//            pemWriter.flush();
//            pemWriter.close();
//            Preferences.putString(Preferences.RSA_PRIVATE_KEY, privateStringWriter.toString());
//        } catch (IOException e) {
//            Log.e("RSAEngine", e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public static PublicKey decodeRSAPublicKeyFromString(String publicKeyPEM)  {

        publicKeyPEM = publicKeyPEM.trim();
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA", "SC");

        byte[] publicKeyBytes = org.spongycastle.util.encoders.Base64.decode(publicKeyPEM.getBytes("UTF-8"));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
        }

    public static PrivateKey decodeRSAPrivateKeyFromString(String privateKeyPEM) throws Exception {

       // privateKeyPEM = privateKeyPEM.trim();
        KeyFactory fact = KeyFactory.getInstance("RSA", "SC");
        byte[] clear = Base64.decode(privateKeyPEM, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }

    public static String stripPublicKeyHeaders(String key) {
        //strip the headers from the key string
        StringBuilder strippedKey = new StringBuilder();
        String lines[] = key.split("\n");
        for (String line : lines) {
            if (!line.contains("BEGIN PUBLIC KEY") && !line.contains("END PUBLIC KEY") && isNullOrEmpty(line.trim())) {
                strippedKey.append(line.trim());
            }
        }
        return strippedKey.toString().trim();
    }

    public static String stripPrivateKeyHeaders(String key) {
        StringBuilder strippedKey = new StringBuilder();
        String lines[] = key.split("\n");
        for (String line : lines) {
            if (!line.contains("BEGIN PRIVATE KEY") && !line.contains("END PRIVATE KEY") && !isNullOrEmpty(line.trim())) {
                strippedKey.append(line.trim());
            }
        }
        return strippedKey.toString().trim();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String createSignature(PrivateKey privateKey, String message) {
        String algorithm = "SHA512withRSA";
        Signature signature = null;
        String signedString = null;

        try {
            signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(message.getBytes());

            byte[] signatureByteArray = signature.sign();

//            signedString = "-----BEGIN SIGNATURE-----"
//                    + DatatypeConverter.printBase64Binary(signatureByteArray)
//                    + "-----END SIGNATURE-----";
        } catch(Exception e) {
            e.printStackTrace();
        }
        return signedString;
    }


    /**
     * hash function
     * @param s
     * @return
     */
    public static String SHA256(String s) {
        try {
            // Create SHA Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

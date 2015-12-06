package com.spentas.javad.securechat.crypto;

import java.security.PrivateKey;

/**
 * Created by javad on 11/24/2015.
 */
public class CkeyPair {

    private CkeyPair pbk;
    private PrivateKey prk;

    public CkeyPair(CkeyPair pbk, PrivateKey prk) {
        this.pbk = pbk;
        this.prk = prk;
    }

    public PrivateKey getPrk() {
        return prk;
    }

    public void setPrk(PrivateKey prk) {
        this.prk = prk;
    }

    public CkeyPair getPbk() {
        return pbk;
    }

    public void setPbk(CkeyPair pbk) {
        this.pbk = pbk;
    }
}

package com.spentas.javad.securechat.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by javad on 11/21/2015.
 */
public class MainThreadBus extends Bus {

    private volatile static MainThreadBus mBus;
    private static Object mLock = new Object();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private MainThreadBus() {
    }

    public static MainThreadBus getInstance() {
        if (mBus == null)
            synchronized (mLock) {
                mBus = new MainThreadBus();
                return mBus;
            }
        return mBus;
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }


}
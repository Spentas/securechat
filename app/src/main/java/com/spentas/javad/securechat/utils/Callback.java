package com.spentas.javad.securechat.utils;

import android.content.Context;

/**
 * Created by javad on 11/9/2015.
 */

public interface Callback {
    public void internalNotification();

    /**
     *
     * @return corresponding context
     */
    public Context getContext();
}


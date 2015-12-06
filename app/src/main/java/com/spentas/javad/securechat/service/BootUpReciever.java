package com.spentas.javad.securechat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by javad on 11/20/2015.
 */
public class BootUpReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context,WsService.class);
        context.startService(startService);
    }
}

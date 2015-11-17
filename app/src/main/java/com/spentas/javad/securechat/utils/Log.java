package com.spentas.javad.securechat.utils;

/**
 * Created by javad on 11/14/2015.
 */
public class Log {
    public static void i(String msg){android.util.Log.i(Const.APP_TAG,msg);}
    public static void e(String msg,Throwable e ){android.util.Log.e(Const.APP_TAG,msg,e);}
}

package com.spentas.javad.securechat.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.spentas.javad.securechat.app.App;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class Utils is a common class that hold many kind of different useful
 * utility methods.
 */
public class Utils {

    //Email Pattern
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    private static Pattern pattern;
    private static Matcher matcher;
    /**
     * Show  progress bar.
     *
     * @param ctx       the ctx
     */
    private static Context context;
    private static ProgressDialog progressDialog;

    public static ProgressDialog progressDialog(Context ctx) {
        if (context != ctx) {
            context = ctx;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            return progressDialog;
        }
        return progressDialog;

    }

    public static Point getDisplayDimension(Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(ctx.getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        return size;
    }


    /**
     * Show dialog.
     *
     * @param ctx       the ctx
     * @param msg       the msg
     * @param btn1      the btn1
     * @param btn2      the btn2
     * @param listener1 the listener1
     * @param listener2 the listener2
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, String msg, String btn1,
                                         String btn2, DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        // builder.setTitle(R.string.app_name);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null && listener2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }


    /**
     * Show dialog.
     *
     * @param ctx       the ctx
     * @param msg       the msg
     * @param btn1      the btn1
     * @param btn2      the btn2
     * @param listener1 the listener1
     * @param listener2 the listener2
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, int msg, int btn1,
                                         int btn2, DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2) {

        return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
                ctx.getString(btn2), listener1, listener2);

    }

    /**
     * Show dialog.
     *
     * @param ctx      the ctx
     * @param msg      the msg
     * @param btn1     the btn1
     * @param btn2     the btn2
     * @param listener the listener
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, String msg, String btn1,
                                         String btn2, DialogInterface.OnClickListener listener) {

        return showDialog(ctx, msg, btn1, btn2, listener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

    }

    /**
     * Show dialog.
     *
     * @param ctx      the ctx
     * @param msg      the msg
     * @param btn1     the btn1
     * @param btn2     the btn2
     * @param listener the listener
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, int msg, int btn1,
                                         int btn2, DialogInterface.OnClickListener listener) {

        return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
                ctx.getString(btn2), listener);

    }

    /**
     * Show dialog.
     *
     * @param ctx      the ctx
     * @param msg      the msg
     * @param listener the listener
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, String msg,
                                         DialogInterface.OnClickListener listener) {

        return showDialog(ctx, msg, ctx.getString(android.R.string.ok), null,
                listener, null);
    }

    /**
     * Show dialog.
     *
     * @param ctx      the ctx
     * @param msg      the msg
     * @param listener the listener
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, int msg,
                                         DialogInterface.OnClickListener listener) {

        return showDialog(ctx, ctx.getString(msg),
                ctx.getString(android.R.string.ok), null, listener, null);
    }

    /**
     * Show dialog.
     *
     * @param ctx the ctx
     * @param msg the msg
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, String msg) {

        return showDialog(ctx, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

    }

    /**
     * Show dialog.
     *
     * @param ctx the ctx
     * @param msg the msg
     * @return the alert dialog
     */
    public static AlertDialog showDialog(Context ctx, int msg) {

        return showDialog(ctx, ctx.getString(msg));

    }

    /**
     * Show dialog.
     *
     * @param ctx      the ctx
     * @param title    the title
     * @param msg      the msg
     * @param listener the listener
     */
    public static void showDialog(Context ctx, int title, int msg,
                                  DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener);
        builder.setTitle(title);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Hide keyboard.
     *
     * @param ctx the ctx
     */
    public static final void hideKeyboard(Activity ctx) {

        if (ctx.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
                    0);
        }
    }

    /**
     * Hide keyboard.
     *
     * @param ctx the ctx
     * @param v   the v
     */
    public static final void hideKeyboard(Activity ctx, View v) {

        try {
            InputMethodManager imm = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for chars > 5 and false for chars < 6 String object
     */
    public static boolean isPasswordValid(String txt) {
        return txt != null && txt.length() > 7 ? true : false;
    }

    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    private static boolean isConnected= false;

    /**
     *
     * @param connectionStatus
     */
    public static void setConnectionStatus(boolean connectionStatus){
        isConnected = connectionStatus;
    }

    /**
     *
     * @return true if is connected to internet
     */
    public static boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getContext().getSystemService(App.getContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        boolean isConnected = networkInfo != null
                && networkInfo.isConnectedOrConnecting();

        boolean isConnected_by_wifi = networkInfoWifi != null
                && networkInfoWifi.isConnectedOrConnecting();
        Utils.setConnectionStatus(isConnected);
        Log.i((isConnected || isConnected_by_wifi) ? String.format("Connected by using 3G=%b || wifi=%b ", isConnected, isConnected_by_wifi) : "Disconnected");

        return isConnected;
    }




}

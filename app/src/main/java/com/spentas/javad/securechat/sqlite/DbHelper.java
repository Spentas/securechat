package com.spentas.javad.securechat.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spentas.javad.securechat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javad on 11/14/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_FRIENDS = "friends";

    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_JID = "jid";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_LAST = "last_Seen";
    public static final String COLUMN_LAST_MSG = "last_msg";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_UNREAD = "unread";
    public static final String COLUMN_USER_PUBLIC_KEY = "pbk";

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + TABLE_FRIENDS + " ADD COLUMN " + COLUMN_USER_PUBLIC_KEY + " string;";
    SQLiteDatabase db;
    Context context;

    private static final String DATABASE_CREATE_FRIEND = "create table "
            + TABLE_FRIENDS + "(" + COLUMN_USER_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_AVATAR + " text," + COLUMN_JID
            + " text," + COLUMN_LAST
            + " text," + COLUMN_TOKEN + " text," + COLUMN_UNREAD + " text,"
            + " text," + COLUMN_LAST_MSG + " text,"+ COLUMN_USER_PUBLIC_KEY + " text);";

    public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_FRIEND);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
       // db.execSQL(DATABASE_ALTER_TEAM_1);
        onCreate(db);
    }

    public void addUsers(List<User> users) {
        db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL(DATABASE_CREATE_FRIEND);
        try {

            ContentValues cv = new ContentValues();

            for (int i = 0; i < users.size(); i++) {

                cv.put(COLUMN_NAME, users.get(i).getUsername());
                cv.put(COLUMN_AVATAR, users.get(i).getImage());
                cv.put(COLUMN_USER_PUBLIC_KEY, users.get(i).getPublickey());

                long r = db.insert(TABLE_FRIENDS, null, cv);
                // SLog.e("INSERTING USERS", r + "");

                cv.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();

    }

    public void addUsers(User user) {
        db = getWritableDatabase();
        try {

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, user.getUsername());
            cv.put(COLUMN_AVATAR, user.getImage());
            cv.put(COLUMN_USER_PUBLIC_KEY, user.getPublickey());
            long r = db.insert(TABLE_FRIENDS, null, cv);

        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();

    }

    public List<User> fetchAllUsers() {
        db = this.getWritableDatabase();
        ArrayList<User> ret = new ArrayList<User>();
        try {
            Cursor c = db.query(TABLE_FRIENDS, new String[]{COLUMN_USER_ID,
                            COLUMN_NAME, COLUMN_JID, COLUMN_AVATAR, COLUMN_LAST,
                            COLUMN_LAST_MSG, COLUMN_UNREAD,COLUMN_USER_PUBLIC_KEY}, null, null, null, null,
                    null);
            int numRows = c.getCount();
            // SLog.e("ROWS COUNT DB USERS", numRows + "");
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                User row = new User();
                row.setUsername(c.getString(1));
                row.setImage(c.getString(3));
                ret.add(row);
                c.moveToNext();
            }
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        db.close();
        return ret;

    }


}

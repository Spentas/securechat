package com.spentas.javad.securechat.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spentas.javad.securechat.crypto.Util;
import com.spentas.javad.securechat.model.Conversation;
import com.spentas.javad.securechat.model.Message;
import com.spentas.javad.securechat.model.User;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by javad on 11/14/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_FRIENDS = "friends";

    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_JID = "id";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_LAST = "last_Seen";
    public static final String COLUMN_LAST_MSG = "last_msg";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_UNREAD = "unread";
    public static final String COLUMN_USER_PUBLIC_KEY = "pbk";
    public static final String COLUMN_SYMMETRIC_KEY="symmetric_key";

    public static final String TABLE_CHAT = "chat";
    public static final String COLUMN_CHAT_ID = "chat_id";
    public static final String COLUMN_FROM = "from_id";
    public static final String COLUMN_TO = "to_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_MSG = "msg";
    public static final String COLUMN_MSG_TYPE = "msg_type";
    public static final String COLUMN_EXTRA = "msg_extra";

    public static final String TABLE_USER="user";
    public static final String COLUMN_USER_NAME="username";
    public static final String COLUMN_USER_PASS="password";
    public static final String COLUMN_USER_PRIVATE_KEY="prk";



    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 10;



    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + TABLE_FRIENDS + " ADD COLUMN " + COLUMN_USER_PUBLIC_KEY + " string;";
    SQLiteDatabase db;
    Context context;

    private static final String DATABASE_CREAT_USER = "create table " + TABLE_USER + "("
            + COLUMN_USER_NAME + " text not null, " + COLUMN_USER_PASS + " text, "
            + COLUMN_USER_PUBLIC_KEY + " text not null, " + COLUMN_AVATAR + " text, "+ COLUMN_USER_PRIVATE_KEY + " text not null);";

    private static final String DATABASE_CREATE_CHAT = "create table "
            + TABLE_CHAT + "(" + COLUMN_CHAT_ID
            + " text not null, " + COLUMN_FROM
            + " text not null, " + COLUMN_TO + " text ," + COLUMN_STATUS
            + " text," + COLUMN_TIME + " text," + COLUMN_MSG_TYPE + " text,"
            + COLUMN_MSG + " text," + COLUMN_EXTRA + " text);";

    private static final String DATABASE_CREATE_FRIEND = "create table "
            + TABLE_FRIENDS + "(" + COLUMN_USER_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_AVATAR + " text," + COLUMN_JID
            + " text," + COLUMN_LAST
            + " text," + COLUMN_TOKEN + " text," + COLUMN_UNREAD + " text,"
            + " text," + COLUMN_LAST_MSG + " text,"+COLUMN_SYMMETRIC_KEY + " text, "+ COLUMN_USER_PUBLIC_KEY + " text);";

    public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_FRIEND);
        db.execSQL(DATABASE_CREATE_CHAT);
        db.execSQL(DATABASE_CREAT_USER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
       // db.execSQL(DATABASE_ALTER_TEAM_1);
        onCreate(db);
    }

    public void addFriends(List<User> users) {
        db = getWritableDatabase();

        try {

            ContentValues cv = new ContentValues();

            for (int i = 0; i < users.size(); i++) {

                cv.put(COLUMN_NAME, users.get(i).getUsername());
                cv.put(COLUMN_AVATAR, users.get(i).getImage());
                cv.put(COLUMN_USER_PUBLIC_KEY, Util.encodeToBase64(users.get(i).getPublicKey().getEncoded()));
                cv.put(COLUMN_USER_PRIVATE_KEY, Util.encodeToBase64(users.get(i).getPrivateKey().getEncoded()));
                long r = db.insert(TABLE_FRIENDS, null, cv);
                // SLog.e("INSERTING USERS", r + "");

                cv.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();

    }

    public void deleteAllFriends(){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, null, null);
    }
    public void addFriend(User user) {
        db = getWritableDatabase();
        try {

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, user.getUsername());
            cv.put(COLUMN_AVATAR, user.getImage());
            cv.put(COLUMN_USER_PUBLIC_KEY,user.getPublicKeyPem());
            cv.put(COLUMN_SYMMETRIC_KEY,Util.encodeToBase64(user.getSecretKey().getEncoded()));
            db.insert(TABLE_FRIENDS, null, cv);

        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();

    }
    public void addFriend(User user,String key) {

        db = getWritableDatabase();
        try {

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, user.getUsername());
            cv.put(COLUMN_USER_PUBLIC_KEY,user.getPublicKeyPem());
            cv.put(COLUMN_SYMMETRIC_KEY, key);
            db.insert(TABLE_FRIENDS, null, cv);
            Log.i("db", "new friend added");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("db", "ERROR in adding new friend to DB");



        }
        db.close();

    }

    public List<User> fetchAllUsers() {
        db = this.getWritableDatabase();
        ArrayList<User> ret = new ArrayList<User>();
        try {
            Cursor c = db.query(TABLE_FRIENDS, new String[]{COLUMN_USER_ID,
                            COLUMN_NAME, COLUMN_JID, COLUMN_AVATAR, COLUMN_LAST,
                            COLUMN_LAST_MSG, COLUMN_UNREAD,COLUMN_USER_PUBLIC_KEY,COLUMN_SYMMETRIC_KEY}, null, null, null, null,
                    null);
            int numRows = c.getCount();
            // SLog.e("ROWS COUNT DB USERS", numRows + "");
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                User row = new User();
                row.setUsername(c.getString(1));
                row.setImage(c.getString(3));
                row.setSecretKey(new SecretKeySpec( Util.decodeFromBase64( c.getString(8)),"AES"));
                ret.add(row);
                c.moveToNext();
            }
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        db.close();
        return ret;

    }

    public long addMessage(Message msg,String chatId) {
        Log.i("DB","add new message  from " + chatId +"  " + msg.getMessage());
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CHAT_ID,chatId);
        cv.put(COLUMN_FROM, msg.getFrom());
        cv.put(COLUMN_TO, msg.getTo());
        cv.put(COLUMN_TIME, msg.getTime());
        cv.put(COLUMN_MSG, msg.getMessage());
        cv.put(COLUMN_MSG_TYPE, msg.getFlag());
        cv.put(COLUMN_EXTRA, msg.getExtra());
        long r = db.insert(TABLE_CHAT, null, cv);
       // addLastMsg(msg);
        db.close();
        return r;
    }

    public List<Message> getChatHistory(String userid, String uid,int page_index_is) {

        List<Message> history = new ArrayList<Message>();
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_CHAT + " WHERE " + COLUMN_FROM
                + " IN ('" + uid + "','" + userid + "') AND " + COLUMN_TO
                + " IN ('" + uid + "','" + userid + "') AND rowid >= (Select MIN(rid) From (Select "+COLUMN_CHAT_ID+" as rid FROM "+ TABLE_CHAT +" WHERE " + COLUMN_FROM
                + " IN ('" + uid + "','" + userid + "') AND " + COLUMN_TO
                + " IN ('" + uid + "','" + userid + "') order by "+COLUMN_CHAT_ID+" desc limit "+ (page_index_is*10) +"))";


        // Log.e("DB",sql);
        //  String query = "AND rowid >= (Select MIN(rid) From (Select rowid as rid FROM "+ TABLE_MESSAGE +" WHERE MsgSenderID='"+ jid +"' AND Me='"+ ConstantData.loginuser.user_id+ "' order by rowid desc limit "+ (page_index_is*25) +"))";


        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Message msg = new Message(c.getLong(0), c.getString(1),
                    c.getString(2), c.getString(4), c.getString(5),
                    c.getString(7), c.getString(6));
            if (msg.getFrom().equals(uid)) {
                msg.setMine(true);
            }
            // SLog.e("Mesage HIst", msg.getMessage() + "|" + msg.getFrom());
            history.add(msg);
            c.moveToNext();
        }
        db.close();
        return history;
    }


    public String getRsaKey(String type){
        String key="";
        try {
            db = this.getWritableDatabase();
            String query = "SELECT * from " + TABLE_USER + ";";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            key = type.equalsIgnoreCase("pbk") ? c.getString(c.getColumnIndex(COLUMN_USER_PUBLIC_KEY)) : c.getString(c.getColumnIndex(COLUMN_USER_PRIVATE_KEY));
            c.close();
        }catch(Exception e){}

        return key;
    }
    public List<Conversation> getChatHistoryById(String chatId) {

        List<Conversation> history = new ArrayList<Conversation>();
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_CHAT + " WHERE " + COLUMN_CHAT_ID + "='"+ chatId + "';";


        Log.e("DB", sql);
        //  String query = "AND rowid >= (Select MIN(rid) From (Select rowid as rid FROM "+ TABLE_MESSAGE +" WHERE MsgSenderID='"+ jid +"' AND Me='"+ ConstantData.loginuser.user_id+ "' order by rowid desc limit "+ (page_index_is*25) +"))";

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()){
            Conversation cv = new Conversation();
            cv.setMsg(c.getString(6));
            cv.setSender(c.getString(1));

//            Message msg = new Message(c.getLong(0), c.getString(1),
//                    c.getString(2), c.getString(4), c.getString(5),
//                    c.getString(7), c.getString(6));

             Log.e("Mesage HIst", cv.getMsg() + "|" + cv.getSender());
            history.add(cv);
           // c.moveToNext();
        }
        db.close();
        return history;
    }

    public boolean addUser(User user) {
       boolean isStored = false;
        db = getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_NAME, user.getUsername());
            cv.put(COLUMN_USER_PUBLIC_KEY, Util.encodeToBase64(user.getPublicKey().getEncoded()));
            cv.put(COLUMN_USER_PRIVATE_KEY, Util.encodeToBase64(user.getPrivateKey().getEncoded()));
            long r = db.insert(TABLE_USER, null, cv);
            isStored = true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();
        return isStored;
    }
    public void updateKey(String key, String username){
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_SYMMETRIC_KEY, key);
            db.update(TABLE_FRIENDS, cv, COLUMN_NAME + "=" + username, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PublicKey getFriendPublicKey(String username){
        String encodedKey="";
        PublicKey pbk = null;
        try {
            db = this.getWritableDatabase();
            String query = "SELECT * from " + TABLE_FRIENDS + " WHERE "+ COLUMN_NAME + "="+username+";";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            encodedKey = c.getString(c.getColumnIndex(COLUMN_USER_PUBLIC_KEY));
            c.close();
           pbk = Util.decodeRSAPublicKeyFromString(encodedKey);

        }catch(Exception e){}

        return pbk;
    }
}

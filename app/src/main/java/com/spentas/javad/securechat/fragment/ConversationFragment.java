package com.spentas.javad.securechat.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.model.Conversation;
import com.spentas.javad.securechat.model.Message;
import com.spentas.javad.securechat.network.websocket.Connection;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.network.websocket.WebSocketClient.Listener;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.spentas.javad.securechat.utils.MainThreadBus;
import com.spentas.javad.securechat.utils.TouchEffect;
import com.spentas.javad.securechat.utils.event.NewMessageEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by javad on 10/30/2015.
 */
public class ConversationFragment extends Activity implements View.OnClickListener, Listener {



    @Inject
    DbHelper mDb;
    @Inject
    SharedPreference sh;
    String me;
    JSONObject jObj;
    private Connection mConnection;
    private List<Conversation> convList;
    private ChatAdapter adp;
    private EditText txt;
    private String buddy;
    private Date lastMsgDate;
    private boolean isRunning;
    private MainThreadBus mTbus;
    public static final TouchEffect TOUCH = new TouchEffect();
    private static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversation);
        ((App) getApplication()).getComponent().inject(this);
        buddy = getIntent().getStringExtra("username");
        me = sh.getUserInfo().get("username");

        convList = mDb.getChatHistoryById(String.format("%s:%s", me, buddy));
        Log.i("conv", String.valueOf(convList.size()));
        ListView list = (ListView) findViewById(R.id.conversation_list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);
        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setTouchNClick(R.id.btnSend);
//        getActionBar().setTitle(buddy);

        handler = new Handler();
        mTbus = MainThreadBus.getInstance();

        mConnection = ConnectionManager.getConnection(ConnectionManager.ConnectionType.WEBSOCKET);
        // ((WsConnection) mConnection).setListener(this);
    }


    @Subscribe
    public void incommingMessage(NewMessageEvent event) {
        Log.i("conv", event.getMsg().getFrom() + buddy);
        Message msg = event.getMsg();
      if (msg.getFrom().equalsIgnoreCase(buddy)) {
        Conversation c = new Conversation();
        c.setMsg(msg.getMessage());
        c.setSender("");
        convList.add(c);
        adp.notifyDataSetChanged();
        }
    }

    public View setTouchNClick(int id) {

        View v = setClick(id);
        if (v != null)
            v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view with given id.
     *
     * @param id the id
     * @return the view on which listener is applied
     */
    public View setClick(int id) {

        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            sendMessage();
        }

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        mTbus.register(this);
        isRunning = true;
        //loadConversationList();
    }

    /* (non-Javadoc)
     * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
     */

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        mTbus.unregister(this);
    }

    private void sendMessage() {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();
        final Conversation c = new Conversation(s, new Date(),
                me);
        c.setStatus(Conversation.STATUS_SENDING);
        convList.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);
        new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... params) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.setFrom(me);
                msg.setTo(buddy);
                msg.setFlag("message");
                msg.setMessage(params[0]);
                mDb.addMessage(msg, String.format("%s:%s", me, buddy));

                mConnection.sendMessageToServer(msg);
                int index = Integer.parseInt(params[1]);
                return index;
            }

            @Override
            protected void onPostExecute(Integer index) {
                super.onPostExecute(index);
                convList.get(index).setStatus(Conversation.STATUS_SENT);
                adp.notifyDataSetChanged();
            }
        }.execute(s, String.valueOf(convList.indexOf(c)));

    }

    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
//    private void loadConversationList()
//    {
//        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
//        if (convList.size() == 0)
//        {
//            // load all messages...
//            ArrayList<String> al = new ArrayList<String>();
//            al.add(buddy);
//            al.add(UserList.user.getUsername());
//            q.whereContainedIn("sender", al);
//            q.whereContainedIn("receiver", al);
//        }
//        else
//        {
//            // load only newly received message..
//            if (lastMsgDate != null)
//                q.whereGreaterThan("createdAt", lastMsgDate);
//            q.whereEqualTo("sender", buddy);
//            q.whereEqualTo("receiver", UserList.user.getUsername());
//        }
//        q.orderByDescending("createdAt");
//        q.setLimit(30);
//        q.findInBackground(new FindCallback<ParseObject>() {
//
//            @Override
//            public void done(List<ParseObject> li, ParseException e)
//            {
//                if (li != null && li.size() > 0)
//                {
//                    for (int i = li.size() - 1; i >= 0; i--)
//                    {
//                        ParseObject po = li.get(i);
//                        Conversation c = new Conversation(po
//                                .getString("message"), po.getCreatedAt(), po
//                                .getString("sender"));
//                        convList.add(c);
//                        if (lastMsgDate == null
//                                || lastMsgDate.before(c.getDate()))
//                            lastMsgDate = c.getDate();
//                        adp.notifyDataSetChanged();
//                    }
//                }
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run()
//                    {
//                        if (isRunning)
//                            loadConversationList();
//                    }
//                }, 1000);
//            }
//        });
//
//    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnect() {
    }

    @Override
    public void onMessage(String message) {

       // new JsonAsyncTask().execute(message);


//        JSONObject jObj = null;
//        try {
//            jObj = new JSONObject(message);
//            String flag = jObj.getString("flag");
//
//            String msg = jObj.getString("message");
//        final Conversation c = new Conversation(message, new Date(),
//                "dummy");
//            c.setMsg(flag.equalsIgnoreCase("self") ? message : msg);
//        convList.add(c);
//        adp.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onMessage(byte[] data) {

    }

    @Override
    public void onDisconnect(int code, String reason) {

    }

    @Override
    public void onError(Exception error) {

    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0) {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            Conversation c = getItem(pos);
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.raw_bubble, null);
                holder = new ViewHolder();
                holder.rootLayout = (LinearLayout) convertView.findViewById(R.id.root_bubble);
                holder.msgLayout = (LinearLayout) convertView.findViewById(R.id.msg_layout);
                holder.msg = (TextView) convertView.findViewById(R.id.lbl2);
                holder.msgTime = (TextView) convertView.findViewById(R.id.lbl1);
                holder.msgStatus = (TextView) convertView.findViewById(R.id.lbl3);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            if (c.getSender().equalsIgnoreCase(me)) {
                holder.msgLayout.setBackgroundResource(R.drawable.right_bubble);
                holder.rootLayout.setGravity(Gravity.RIGHT);
            } else {
                holder.msgLayout.setBackgroundResource(R.drawable.left_bubble);
                holder.rootLayout.setGravity(Gravity.LEFT);

            }

            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
//            lbl.setText(DateUtils.getRelativeDateTimeString(ConversationFragment.this, c
//                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
//                    DateUtils.DAY_IN_MILLIS, 0));
            holder.msgTime.setText(df.format(Calendar.getInstance().getTime()));

            holder.msg.setText(c.getMsg());


            if (c.getSender().equalsIgnoreCase("User")) {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    holder.msgStatus.setText("Sent");
                else if (c.getStatus() == Conversation.STATUS_SENDING)
                    holder.msgStatus.setText("Sending...");
                else
                    holder.msgStatus.setText("Failed");
            } else
                holder.msgStatus.setText("");

            return convertView;
        }


        private class ViewHolder {
            TextView msgTime, msg, msgStatus;
            LinearLayout rootLayout;
            LinearLayout msgLayout;
        }


    }


    private class JsonAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String s = params[0];
            Conversation c = new Conversation(s, new Date(),
                    "dummy");
            c.setMsg(s);
            convList.add(c);


            return s;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            adp.notifyDataSetChanged();
            super.onPostExecute(jsonString);
        }
    }


}

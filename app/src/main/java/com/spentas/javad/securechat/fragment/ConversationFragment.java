package com.spentas.javad.securechat.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.utils.TouchEffect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.spentas.javad.securechat.model.Conversation;

/**
 * Created by javad on 10/30/2015.
 */
public class ConversationFragment extends Activity implements View.OnClickListener {
    private ArrayList<Conversation> convList;
    public static final TouchEffect TOUCH = new TouchEffect();

    /** The chat adapter. */
    private ChatAdapter adp;

    /** The Editext to compose the message. */
    private EditText txt;

    /** The user name of buddy. */
    private String buddy;

    /** The date of last message in conversation. */
    private Date lastMsgDate;

    /** Flag to hold if the activity is running or not. */
    private boolean isRunning;

    /** The handler. */
    private static Handler handler;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversation);

        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.conversation_list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

//        buddy = getIntent().getStringExtra(Const.EXTRA_DATA);
//        getActionBar().setTitle(buddy);

        handler = new Handler();
    }

    public View setTouchNClick(int id)
    {

        View v = setClick(id);
        if (v != null)
            v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view with given id.
     *
     * @param id
     *            the id
     * @return the view on which listener is applied
     */
    public View setClick(int id)
    {

        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnSend)
        {
            sendMessage();
        }

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        //loadConversationList();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
    }

    /* (non-Javadoc)
     * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
     */


    /**
     * Call this method to Send message to opponent. It does nothing if the text
     * is empty otherwise it creates a Parse object for Chat message and send it
     * to Parse server.
     */
    private void sendMessage()
    {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();
//		final Conversation c = new Conversation(s, new Date(),
//				UserList.user.getUsername());
//		c.setStatus(Conversation.STATUS_SENDING);
        final Conversation c = new Conversation(s, new Date(),
                "User");
        c.setStatus(Conversation.STATUS_SENDING);
        convList.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);

//		ParseObject po = new ParseObject("Chat");
//		po.put("sender", UserList.user.getUsername());
//		po.put("receiver", buddy);
//		// po.put("createdAt", "");
//		po.put("message", s);
//		po.saveEventually(new SaveCallback() {
//			@Override
//			public void done(ParseException e)
//			{
//
//				if (e == null)
//					c.setStatus(Conversation.STATUS_SENT);
//				else
//					c.setStatus(Conversation.STATUS_FAILED);
//				adp.notifyDataSetChanged();
//			}
//		});
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

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class  ChatAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0)
        {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.raw_conversation_sender, null);
            else
                v = getLayoutInflater().inflate(R.layout.raw_conversation_reciever, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            SimpleDateFormat  df = new SimpleDateFormat("h:mm a");
//            lbl.setText(DateUtils.getRelativeDateTimeString(ConversationFragment.this, c
//                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
//                    DateUtils.DAY_IN_MILLIS, 0));
            lbl.setText(df.format(Calendar.getInstance().getTime()));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent())
            {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText("Delivered");
                else if (c.getStatus() == Conversation.STATUS_SENDING)
                    lbl.setText("Sending...");
                else
                    lbl.setText("Failed");
            }
            else
                lbl.setText("");

            return v;
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

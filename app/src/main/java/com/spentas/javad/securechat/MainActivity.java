package com.spentas.javad.securechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.spentas.javad.securechat.adapter.FriendsListAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.fragment.ConversationFragment;

/**
 * Created by javad on 10/29/2015.
 */
public class MainActivity extends AppCompatActivity {
    App  app;
    @Bind(R.id.friendlist)
    ListView friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App)getApplication();

        setContentView(R.layout.fragment_friendlist);
        ButterKnife.bind(this);
        friendList.setAdapter(new FriendsListAdapter(this, dummyList()));
    }

    @OnItemClick(R.id.friendlist) void onListItemClick(int positoin){
        startActivity(new Intent(this, ConversationFragment.class));
    }
    public List<String> dummyList() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add("Javad");
            list.add("Farzad");
            list.add("Roham");
            list.add("Ebi");
            list.add("Poria");
        }
        return list;
    }
    @Override
    protected void onPause() {
        app.getConnection().disConnect();
        System.out.println(app.getConnection().isConnected());
        super.onPause();

    }

    @Override
    protected void onResume() {
        app.getConnection().connect();
        System.out.println(app.getConnection().isConnected());
        super.onResume();
    }
}

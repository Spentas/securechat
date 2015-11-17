package com.spentas.javad.securechat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.loopj.android.http.RequestParams;
import com.spentas.javad.securechat.adapter.ViewPagerAdapter;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.fragment.FindFriendFragment;
import com.spentas.javad.securechat.fragment.FriendListFragment;
import com.spentas.javad.securechat.fragment.SearchDialog;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.network.webservice.RestfulRequest;
import com.spentas.javad.securechat.network.websocket.Connection;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.utils.Callback;
import com.spentas.javad.securechat.utils.Log;
import com.spentas.javad.securechat.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by javad on 10/29/2015.
 */
public class MainActivity extends AppCompatActivity implements OnQueryTextListener, Callback {

    private static Menu mMenu;
    @Inject
    ConnectionManager mConnectionManager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private Connection mConnection;
    private SearchDialog dialogFragment;
    private Bundle bundle;
    private List<User> searchResult;
    private JSONArray jsonArray;
    private int[] mTabIcon = {R.drawable.ic_account_multiple, R.drawable.ic_account_search};
    private SearchView mSearchView;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon(tabLayout);
        com.spentas.javad.securechat.utils.Log.i(String.valueOf(Utils.isConnected()));
        //
        // Log.i("info",connection.toString());
        viewPager.clearOnPageChangeListeners();

        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        mFragmentManager = getSupportFragmentManager();
        mConnection = mConnectionManager.getConnection(ConnectionManager.ConnectionType.WEBSOCKET);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        mMenu = menu;
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    public void setupTabIcon(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(mTabIcon[0]);
        tabLayout.getTabAt(1).setIcon(mTabIcon[1]);

    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter vpadapter = new ViewPagerAdapter(getSupportFragmentManager());

        vpadapter.addFragment(new FriendListFragment(), getString(R.string.tab1));
        vpadapter.addFragment(new FindFriendFragment(), getString(R.string.tab2));
        viewPager.setAdapter(vpadapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("on activity result");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        mConnection.disConnect();
        Log.i(String.format("Connection status : %b", mConnection.isConnected()));
        super.onPause();

    }

    @Override
    protected void onResume() {

        mConnection.connect();
        Log.i(String.format("Connection status : %b", mConnection.isConnected()));
        super.onResume();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        RequestParams params = new RequestParams();
        params.put("username", query);
        params.put("token", "pass");
        RestfulRequest.sendRequest(params, this, RestfulRequest.RequestType.FINDFRIEND);


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void internalNotification(JSONObject object) {
        String json = null;
        try {
            if (object.getString("tag").equalsIgnoreCase(RestfulRequest.RequestType.FINDFRIEND.toString()))
                if (object.getBoolean("status")) {
                    json = object.getString("result");
                    dialogFragment = SearchDialog.newInstance();
                    bundle = new Bundle();
                    bundle.putString("result", json);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(mFragmentManager, SearchDialog.DIALOG_TAG);
                } else
                    Utils.showDialog(this, "Friend not found.");


        } catch (JSONException e) {
            Utils.showDialog(this, "Unexcepted Error! Try again.");

            e.printStackTrace();
        }


    }

    @Override
    public Context getContext() {
        return this;
    }


    private static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final WeakReference<TabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            final TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                final boolean updateText = (mScrollState == ViewPager.SCROLL_STATE_DRAGGING)
                        || (mScrollState == ViewPager.SCROLL_STATE_SETTLING
                        && mPreviousScrollState == ViewPager.SCROLL_STATE_DRAGGING);
                tabLayout.setScrollPosition(position, positionOffset, updateText);
            }
        }

        @Override
        public void onPageSelected(int position) {

            mMenu.findItem(R.id.action_search).setVisible((position == 1) ? false : false);
            final TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                tabLayout.getTabAt(position).select();
            }
        }
    }
}

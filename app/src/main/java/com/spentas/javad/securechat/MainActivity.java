package com.spentas.javad.securechat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.spentas.javad.securechat.adapter.FriendListAdapter;
import com.spentas.javad.securechat.adapter.ViewPagerAdapter;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.fragment.FindFriendFragment;
import com.spentas.javad.securechat.fragment.FriendListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by javad on 10/29/2015.
 */
public class MainActivity extends AppCompatActivity implements OnQueryTextListener {
    App app;

    private int[] mTabIcon={R.drawable.ic_account_multiple,R.drawable.ic_account_search};
    private SearchView mSearchView;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon(tabLayout);

       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater  menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
       // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    public void setupTabIcon(TabLayout tabLayout){
        tabLayout.getTabAt(0).setIcon(mTabIcon[0]);
        tabLayout.getTabAt(1).setIcon(mTabIcon[1]);

    }
    public void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter vpadapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpadapter.addFragment(new FriendListFragment(),getString(R.string.tab1));
        vpadapter.addFragment(new FindFriendFragment(),getString(R.string.tab2));
        viewPager.setAdapter(vpadapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("on activity result");
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("submit");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("change");
        return false;
    }
}

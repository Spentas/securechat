package com.spentas.javad.securechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spentas.javad.securechat.MainActivity;
import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.adapter.FriendListAdapter;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.utils.event.DataSetChangeEvent;
import com.spentas.javad.securechat.utils.DividerItemDecoration;
import com.spentas.javad.securechat.utils.event.FragmentCallback;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static com.spentas.javad.securechat.R.id;
import static com.spentas.javad.securechat.R.layout;

/**
 * Created by javad on 11/11/2015.
 */
public class FriendListFragment extends Fragment {

    @Inject
    Bus bus;
    @Inject
    DbHelper mDb;
    @Bind(id.friendlist)
    RecyclerView mRecyclerView;

    private Object mLock = new Object();
    private static final String TAG = "RecyclerViewFragment";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private FriendListAdapter mFriendListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<User> mFriends;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layout.fragment_friendlist, container, false);
        ButterKnife.bind(this, view);
        ((App) App.getContext()).getComponent().inject(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mFriends = mDb.fetchAllUsers();
        mFriendListAdapter = new FriendListAdapter(getActivity(),mFriends);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFriendListAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }



    @Produce FragmentCallback fragmentCallback(){
        return new FragmentCallback();
    }

    @Subscribe public void onDataSetChangeEvent(DataSetChangeEvent event){
        mFriends.clear();
       synchronized (mLock) {
           mFriends.addAll(mDb.fetchAllUsers());
       }
        mFriendListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }


}

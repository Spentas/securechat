package com.spentas.javad.securechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spentas.javad.securechat.adapter.FriendListAdapter;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.spentas.javad.securechat.utils.DividerItemDecoration;
import com.spentas.javad.securechat.utils.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.spentas.javad.securechat.R.id;
import static com.spentas.javad.securechat.R.layout;

/**
 * Created by javad on 11/11/2015.
 */
public class FriendListFragment extends Fragment {

    @Inject
    SharedPreference mSharedPreference;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    protected RecyclerView mRecyclerView;
    protected FriendListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Bind(id.friendlist)
    RecyclerView friendList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_friendlist, container, false);
        ButterKnife.bind(this, view);

//
//        if (savedInstanceState != null) {
//            // Restore saved layout manager type.
//            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }
        friendList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        friendList.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendList.setAdapter(new FriendListAdapter(getActivity(), dummyList()));
        friendList.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    //    @OnItemClick(R.id.friendlist) void onListItemClick(int positoin){
//        startActivity(new Intent(this, ConversationFragment.class));
//    }
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



    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}

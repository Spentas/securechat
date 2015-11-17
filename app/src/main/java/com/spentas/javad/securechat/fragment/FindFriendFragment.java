package com.spentas.javad.securechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.RequestParams;
import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.adapter.SearchListAdapter;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.network.webservice.RestfulRequest;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.utils.Callback;
import com.spentas.javad.securechat.utils.DividerItemDecoration;
import com.spentas.javad.securechat.utils.Utils;
import com.squareup.otto.Bus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javad on 11/11/2015.
 */
public class FindFriendFragment extends Fragment implements Callback {

    @Inject
    Bus bus;
    @Inject
    DbHelper mDb;
    @Bind(R.id.search_txt)
    EditText mSearchBox;
    @Bind(R.id.search_result_list)
    RecyclerView mRecyclerView;
    private SearchDialog mDialogFragment;
    private Bundle mBundle;
    private List<User> mSearchResult;
    private FragmentManager mFragmentManager;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchListAdapter mSearchListAdapter;
    private RequestParams params;
    private ObjectMapper mObjectMapper;


    public FindFriendFragment() {
        //used by tablayout
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) App.getContext()).getComponent().inject(this);
        mObjectMapper = new ObjectMapper();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_friend, container, false);
        mFragmentManager = getFragmentManager();
        ButterKnife.bind(this, v);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchResult = new ArrayList<>();
        mSearchListAdapter = new SearchListAdapter(getActivity(), mSearchResult);
        mRecyclerView.setAdapter(mSearchListAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @OnClick(R.id.search_go)
    public void findFriends() {
        params = new RequestParams();
        params.put("username", mSearchBox.getText().toString());
        params.put("token", "pass");
        RestfulRequest.sendRequest(params, this, RestfulRequest.RequestType.FINDFRIEND);
    }

    @Override
    public void httpCallback(JSONObject object) {
        String json = null;
        try {
            if (object.getString("tag").equalsIgnoreCase(RestfulRequest.RequestType.FINDFRIEND.toString()))
                if (object.getBoolean("status")) {
                    json = object.getString("result");
                    mSearchResult = new ArrayList<User>(Arrays.asList(mObjectMapper.readValue(json, User[].class)));
                    mSearchListAdapter.clear();
                    mSearchListAdapter.addAll(mSearchResult);
                    mSearchListAdapter.notifyDataSetChanged();
                } else
                    Utils.showDialog(this.getActivity(), "Friend not found.");


        } catch (Exception e) {
            Utils.showDialog(this.getActivity(), "Unexcepted Error! Try again.");

            e.printStackTrace();
        }
    }





}

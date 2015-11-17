package com.spentas.javad.securechat.fragment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.adapter.SearchListAdapter;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.utils.DividerItemDecoration;
import com.spentas.javad.securechat.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by javad on 11/12/2015.
 */
public class SearchDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String DIALOG_TAG = "dialog";
    private LinearLayout mRoot;
    private de.hdodenhof.circleimageview.CircleImageView mUserImage;
    private android.support.v7.widget.RecyclerView mRecyclerView;
    private Activity context;
    private Point mSize;
    private Window mWindow;
    private List<User> mSearchResult;


    public static SearchDialog newInstance() {
        return new SearchDialog();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        mRoot = new LinearLayout(context);
        mRoot.setOrientation(LinearLayout.VERTICAL);
        mRoot.setLayoutParams(params);
        mRoot.setBackgroundColor(getResources().getColor(R.color.white));
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(params);
        mRoot.addView(mRecyclerView);
        Bundle bundle = getArguments();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
           mSearchResult = new ArrayList<User>(Arrays.asList(objectMapper.readValue(bundle.getString("result"), User[].class)));
        }catch (Exception e){
            Log.e(DIALOG_TAG,"un expected error in extracting search result");}

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchListAdapter adapter = new SearchListAdapter(context, mSearchResult);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        getDialog().getWindow().requestFeature(getDialog().getWindow().FEATURE_NO_TITLE);
        return mRoot;


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


public void getResult(){

}

    @Override
    public void onResume() {
        super.onResume();
        mWindow = getDialog().getWindow();
        mWindow.setLayout(mSize.x - 50, mSize.y - 200);
        mWindow.setGravity(Gravity.CENTER);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        mSize = Utils.getDisplayDimension(context);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

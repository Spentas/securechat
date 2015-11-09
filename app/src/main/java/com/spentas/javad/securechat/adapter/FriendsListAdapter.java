package com.spentas.javad.securechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spentas.javad.securechat.R;

import java.util.List;

import butterknife.Bind;

/**
 * Created by javad on 10/30/2015.
 */
public class FriendsListAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mUsers;

        public FriendsListAdapter(Context mContext, List<String> mUsers) {
            this.mContext = mContext;
            this.mUsers = mUsers;
        }


        @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.raw_friendlist, null);
            holder = new ViewHolder();
            holder.userName = (TextView)convertView.findViewById(R.id.friendlist_username);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.userName.setText(mUsers.get(position));
        return convertView;
    }

    static class ViewHolder{
        @Bind(R.id.frienlist_profile_image)
        de.hdodenhof.circleimageview.CircleImageView profileImage;
        TextView userName;
    }
}

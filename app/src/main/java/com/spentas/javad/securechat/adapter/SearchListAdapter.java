package com.spentas.javad.securechat.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spentas.javad.securechat.R;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.model.Message;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.spentas.javad.securechat.utils.MainThreadBus;
import com.spentas.javad.securechat.utils.event.DataSetChangeEvent;
import com.spentas.javad.securechat.view.cpb.CircularProgressButton;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by javad on 11/12/2015.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private final Object mLock = new Object();
    @Inject
    Bus bus;
    @Inject
    DbHelper mDb;
    @Inject
    SharedPreference mSh;
    private MainThreadBus mTbus;
    private Context mContext;
    private List<User> mUsers;

    public SearchListAdapter(Context mContext, List<User> mUsers) {
        ((App) App.getContext()).getComponent().inject(this);
        this.mContext = mContext;
        this.mUsers = mUsers;
        mTbus = MainThreadBus.getInstance();
        mTbus.register(this);
    }
    View view;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_searchlist, parent, false);
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.username.setText(mUsers.get(position).getUsername());

        Log.i("Found friens", String.format("username : %s \n public key :\n %s",mUsers.get(position).getUsername(),mUsers.get(position).getPublicKey()));

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addAll(List<User> collection) {
        synchronized (mLock) {
            mUsers.addAll(collection);
        }
    }

    public void clear() {
        synchronized (mLock) {
           mUsers.clear();
        }

    }

    @Override
    protected void finalize() throws Throwable {
        mTbus.unregister(this);
        super.finalize();
    }



    private static class ProgressBar extends AsyncTask<Integer, Integer, Integer> {
        private CircularProgressButton btn;

        public ProgressBar(CircularProgressButton btn ) {
            this.btn = btn;
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            for (int progress = 0; progress <= 100; progress += 1) {
                publishProgress(progress);
                try {
                    if (progress == 1)
                        Thread.sleep(50);
                    else
                        Thread.sleep(5);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            btn.setProgress(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            btn.setProgress(values[0]);
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.frienlist_profile_image)
        de.hdodenhof.circleimageview.CircleImageView profileImage;
        @Bind(R.id.friendlist_username)
        TextView username;
        @Bind(R.id.send_frireq_btn)
        CircularProgressButton button;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.friendlist_username);
            button = (CircularProgressButton) itemView.findViewById(R.id.send_frireq_btn);
            button.setOnClickListener(this);

        }

        @Produce
        public DataSetChangeEvent produceDataSetChangeEvent(){
           return new DataSetChangeEvent();
        }

        @Override
        public void onClick(View v) {
            new ProgressBar((CircularProgressButton) v).execute(100);
            int position = getLayoutPosition();
            User user = mUsers.get(position);
            mDb.addFriend(user);
            mTbus.post(produceDataSetChangeEvent());
            Message msg = new Message();
            msg.setTo(mUsers.get(position).getUsername());
            msg.setFrom(mSh.getUserInfo().get("username"));
            msg.setFlag("skey");
            msg.setMessage("aes");
            msg.setPublicKey(mDb.getRsaKey("pbk"));
            ConnectionManager.getConnection(ConnectionManager.ConnectionType.WEBSOCKET).sendMessageToServer(msg);
        }
    }


}

package com.spentas.javad.securechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.network.websocket.Connection;
import com.spentas.javad.securechat.network.NetworkConfig;
import com.spentas.javad.securechat.network.webservice.RestfulRequest;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.spentas.javad.securechat.utils.Utils;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by javad on 10/29/2015.
 */
public class LoginActivity extends ActionBarActivity implements com.spentas.javad.securechat.utils.Callback {

    private App app;


    @Inject
    SharedPreference sharedPreference;
    @Bind(R.id.login_username_txt)
    EditText mUsername;
    @Bind(R.id.login_password_txt)
    EditText mPassword;
    @Bind(R.id.login_password_layout)
    TextInputLayout mPasswordLayout;
    @Bind(R.id.login_username_layout)
    TextInputLayout mUsernameLayout;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        //ws connection
        app = (App)getApplication();
        Connection connection = app.getConnection();
        System.out.println(connection.getReference());
        System.out.println(connection.isConnected());
        mUsername.addTextChangedListener(new CustomTextWatcher(mUsername));
        mPassword.addTextChangedListener(new CustomTextWatcher(mPassword));

    }

    @OnClick(R.id.btnLogin)
    public void loginOnClick(View view) {
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        RequestParams params = new RequestParams();
        if (Utils.isNotNull(username) && Utils.isNotNull(password)){
        if (!Utils.validate(username)) {
            params.put(NetworkConfig.REST_PASSWORD_PARAM, password);
            params.put(NetworkConfig.REST_USERNAME_PARAM, username);
            RestfulRequest.login(params,this);
        }
        } else
            Toast.makeText(this,"Please fill the form, don't leave any field blank",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.btnReg)
    public void registerOnClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

    }



    @Override
    public void internalNotification(JSONObject object) {
        sharedPreference.storeLoginInfo(username,password);
        sharedPreference.storeLoginStatus(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public Context getContext() {
        return this;
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
    public void onBackPressed() {
        //block back btn
    }

    public class CustomTextWatcher implements TextWatcher{

        private View mView;
        public CustomTextWatcher(View v){
            this.mView = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (mView.getId()){
                case (R.id.login_username_txt) :
                    if (!Utils.isNotNull(mUsername.getText().toString())) {
                        mUsername.setError(getString(R.string.error_field_required));
                        mUsername.requestFocus();
                    }
                    else
                        mUsernameLayout.setErrorEnabled(false);

                    break;
                case (R.id.login_password_txt):
                    if (!Utils.isNotNull(mPassword.getText().toString())) {
                        mPassword.setError(getString(R.string.error_field_required));
                        mPassword.requestFocus();
                    }
                    else
                        mPasswordLayout.setErrorEnabled(false);
                    break;
            }
        }
    }

}

package com.spentas.javad.securechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.spentas.javad.securechat.network.NetworkConfig;
import com.spentas.javad.securechat.network.webservice.RestfulRequest;
import com.spentas.javad.securechat.utils.Callback;
import com.spentas.javad.securechat.utils.Utils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javad on 10/29/2015.
 */
public class RegistrationActivity extends AppCompatActivity implements Callback {

    @Bind(R.id.register_username_txt)
    EditText mUsername;
    @Bind(R.id.register_pass_txt)
    EditText mPassword;
    @Bind(R.id.register_confirmpass_txt)
    EditText mPasswordConfirmation;
    @Bind(R.id.register_pass_layout)
    TextInputLayout mPasswordLayout;
    @Bind(R.id.register_username_layout)
    TextInputLayout mUsernameLayout;
    @Bind(R.id.register_passconfirm_layout)
    TextInputLayout mPasswordConfirmationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        mUsername.addTextChangedListener(new CustomTextWatcher(mUsername));
        mPassword.addTextChangedListener(new CustomTextWatcher(mPassword));
        mPasswordConfirmation.addTextChangedListener(new CustomTextWatcher(mPasswordConfirmation));
    }

    @Override
    public void httpCallback(JSONObject object) {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick(R.id.btnReg)
    public void onRegiterClick() {

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (Utils.isNotNull(username) && Utils.isNotNull(password) && mPasswordConfirmation.getText().toString().compareTo(mPassword.getText().toString()) == 0) {
            if (!Utils.validate(username) && Utils.isPasswordValid(mPassword.getText().toString())) {
                RequestParams params = new RequestParams();
                params.put(NetworkConfig.REST_PUBLIC_KEY_PARAM,"public key");
                params.put(NetworkConfig.REST_PASSWORD_PARAM, password);
                params.put(NetworkConfig.REST_USERNAME_PARAM, username);
                RestfulRequest.register(params, this);
            }
        } else
            Toast.makeText(this, "Please fill the form, don't leave any field blank", Toast.LENGTH_SHORT).show();

    }


    public class CustomTextWatcher implements TextWatcher {

        private View mView;

        public CustomTextWatcher(View v) {
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
            switch (mView.getId()) {
                case (R.id.register_username_txt):
                    if (!Utils.isNotNull(mUsername.getText().toString())) {
                        mUsername.setError(getString(R.string.error_field_required));
                        mUsername.requestFocus();
                    } else
                        mUsernameLayout.setErrorEnabled(false);
                    break;
                case (R.id.register_pass_txt):
                    if (!Utils.isNotNull(mPassword.getText().toString())) {
                        mPassword.setError(getString(R.string.error_field_required));
                        mPassword.requestFocus();
                    } else if (!Utils.isPasswordValid(mPassword.getText().toString())) {
                        mPassword.setError(getString(R.string.error_password_inadequatchar));
                        mPassword.requestFocus();
                    } else
                        mPasswordLayout.setErrorEnabled(false);
                    break;
                case (R.id.register_confirmpass_txt):
                    if (!Utils.isNotNull(mPasswordConfirmation.getText().toString())) {
                        mPasswordConfirmation.setError(getString(R.string.error_field_required));
                        mPasswordConfirmation.requestFocus();
                    } else if (mPasswordConfirmation.getText().toString().compareTo(mPassword.getText().toString()) != 0) {
                        mPasswordConfirmation.setError(getString(R.string.error_pass_notmatch));
                        mPasswordConfirmation.requestFocus();
                    } else
                        mPasswordLayout.setErrorEnabled(false);
                    break;
            }
        }
    }
}

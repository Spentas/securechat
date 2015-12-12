package com.spentas.javad.securechat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.spentas.javad.securechat.app.App;
import com.spentas.javad.securechat.model.Message;
import com.spentas.javad.securechat.model.User;
import com.spentas.javad.securechat.network.websocket.ConnectionManager;
import com.spentas.javad.securechat.sqlite.DbHelper;
import com.spentas.javad.securechat.sqlite.SharedPreference;
import com.spentas.javad.securechat.view.KenBurnsView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {
    @Inject
    DbHelper db;

    private final int SPLASH_DISPLAY_LENGTH = 9000;
    @Inject
    SharedPreference sharedPreference;
    @Bind(R.id.splash_img)
    KenBurnsView mSplashBackground;
    @Bind(R.id.logo)
    ImageView mLogo;
    @Bind(R.id.welcome_text)
    TextView welcomeText;
    private boolean mLoginStaus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        //db.deleteAllFriends();


        mLoginStaus = sharedPreference.getLoginStatus();
        animation();

    }


    private void animation() {
        mLogo.setAlpha(0.7F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        mLogo.startAnimation(anim);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
        alphaAnimation.setStartDelay(1000);
        alphaAnimation.setDuration(800);
        alphaAnimation.start();
        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(mLoginStaus ? new Intent(SplashActivity.this, MainActivity.class) : new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

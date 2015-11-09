package com.spentas.javad.securechat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.spentas.javad.securechat.view.KenBurnsView;


import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 9000;
    @Bind(R.id.splash_img)
    KenBurnsView mSplashBackground;
    @Bind(R.id.logo)
    ImageView mLogo;
    @Bind(R.id.welcome_text)
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
       // mSplashBackground.setImageResource(R.drawable.splash_image);
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
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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

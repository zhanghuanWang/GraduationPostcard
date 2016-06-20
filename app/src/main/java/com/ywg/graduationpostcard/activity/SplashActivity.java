package com.ywg.graduationpostcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.utils.AppUtils;

/**
 * 闪屏Activity
 */
public class SplashActivity extends AppCompatActivity {

    private TextView mTvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);

        mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        mTvAppVersion.setText(String.valueOf("V " + AppUtils.getAppVersionName(this)));
    }
}

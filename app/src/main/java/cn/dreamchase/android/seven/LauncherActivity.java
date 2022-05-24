package cn.dreamchase.android.seven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import cn.dreamchase.android.seven.home.HomeActivity;
import cn.dreamchase.android.seven.launcher.FirstLauncherActivity;

public class LauncherActivity extends AppCompatActivity {

    public static final String FIRST_LAUNCHER = "first_launcher"; // 是否第一次启动
    private final long waitTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchr);

        start();
    }

    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isFirstLauncher()) {
                    intent = new Intent(LauncherActivity.this, HomeActivity.class);
                    // 为true时第二次启动，因为第一次启动时会把FIRST_LAUNCHER变为true
                }else {
                    intent = new Intent(LauncherActivity.this, FirstLauncherActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },waitTime);
    }

    public boolean isFirstLauncher() {
        SharedPreferences sharedPreferences = getSharedPreferences("android", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(FIRST_LAUNCHER,false);
    }
}
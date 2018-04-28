package com.flag.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.flag.FelixApplication;


/**
 * Created by Mykola Matsiakh on 06.09.17.
 * Copyright (c) 2017, Reynolds. All rights reserved.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);
        int SPLASH_TIME_OUT = 100;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            //  Create a new boolean and preference and set it to true

            @Override
            public void run() {
              //  checkFirstRun();
                checkFirstRun();
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkFirstRun() {
        final String PREFS_NAME = "MyPrefsUser";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedUserInt = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        if (savedUserInt == 1) {
            Intent i = new Intent(SplashActivity.this, MarkerActivity.class);
            startActivity(i);

        } else if (savedUserInt == DOESNT_EXIST) {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
        }
    }
}


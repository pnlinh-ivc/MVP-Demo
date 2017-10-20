package com.pnlinh.mvpdemo;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.pnlinh.mvpdemo.data.SharedPrefsHelper;

/**
 * Created by pnlinh on 10/20/2017.
 */

public class MyApplication extends Application {
    private SharedPrefsHelper mSharedPrefsHelper;
    private static final String PREF_NAME = "pnlinh_handsome";

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSharedPrefsHelper = new SharedPrefsHelper(getSharedPreferences(PREF_NAME, MODE_PRIVATE));
    }

    public SharedPrefsHelper getSharedPrefsHelper() {
        return mSharedPrefsHelper;
    }
}

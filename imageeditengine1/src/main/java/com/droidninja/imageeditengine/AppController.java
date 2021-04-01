package com.droidninja.imageeditengine;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
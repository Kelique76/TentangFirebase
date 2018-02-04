package com.kelique.allnewfireapp;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by kelique on 2/1/2018.
 */

public class MainApplication extends MultiDexApplication {


    private static MainApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    public static synchronized MainApplication getInstance() {
        return mainApplication;
    }
}
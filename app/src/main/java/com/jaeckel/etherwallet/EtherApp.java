package com.jaeckel.etherwallet;

import android.app.Application;

import com.novoda.notils.logger.simple.Log;

public class EtherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.setShowLogs(true);
        Log.d("onCreate()");
    }
}

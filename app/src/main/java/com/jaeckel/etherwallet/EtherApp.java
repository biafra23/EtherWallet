package com.jaeckel.etherwallet;

import android.app.Application;

import com.jaeckel.etherwallet.util.DebugTree;

import timber.log.Timber;

public class EtherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new DebugTree());
//        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate()");
    }
}

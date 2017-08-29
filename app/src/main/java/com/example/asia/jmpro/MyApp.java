package com.example.asia.jmpro;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by asia on 29/08/2017.
 *
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

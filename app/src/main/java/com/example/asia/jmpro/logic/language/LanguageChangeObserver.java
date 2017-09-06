package com.example.asia.jmpro.logic.language;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asia on 06/09/2017.
 */

public class LanguageChangeObserver implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Activity activity;

    public LanguageChangeObserver(Activity activity) {
        this.activity = activity;
    }

    public LanguageChangeObserver start() {
        usersData().registerOnSharedPreferenceChangeListener(this);
        return this;
    }

    public void stop() {
        usersData().unregisterOnSharedPreferenceChangeListener(this);
    }

    private SharedPreferences usersData() {
        return activity.getBaseContext().getSharedPreferences("UsersData", MODE_PRIVATE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("languageStr")) {
            activity.recreate();
        }
    }
}

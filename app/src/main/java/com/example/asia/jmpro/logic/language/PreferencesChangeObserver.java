package com.example.asia.jmpro.logic.language;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.asia.jmpro.MyApp;
import com.example.asia.jmpro.R;
import com.example.asia.jmpro.SettingsFragment3;
import com.example.asia.jmpro.logic.location.LocationChangeObserver;
import com.example.asia.jmpro.logic.theme.CurrentThemeHolder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asia on 06/09/2017.
 */

public class PreferencesChangeObserver implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Activity activity;

    public PreferencesChangeObserver(Activity activity) {
        this.activity = activity;
    }

    public PreferencesChangeObserver start() {
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

        if (key.equals("notificationsStatus")) {
            activity.stopService(new Intent(activity, LocationChangeObserver.class));
        }

        if(key.equals("selectedThemeName") || key.equals("selectedThemeItem"))
        {
            MyApp.reloadTheme(activity);
            activity.recreate();
        }
    }
}

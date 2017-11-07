package com.example.asia.jmpro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.example.asia.jmpro.logic.theme.CurrentThemeHolder;

import io.realm.Realm;

/**
 * Created by asia on 29/08/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public static void reloadTheme(Activity activity)
    {
        SharedPreferences prefs = activity.getSharedPreferences("UsersData", MODE_PRIVATE);
        CurrentThemeHolder.getInstance().setTheme(prefs.getInt(SettingsFragment3.PREFERENCES_THEME_NAME, R.style.AppTheme));
    }

    public static int getThemeId(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("UsersData", MODE_PRIVATE);
        return prefs.getInt(SettingsFragment3.PREFERENCES_THEME_NAME, R.style.AppTheme);
    }

}





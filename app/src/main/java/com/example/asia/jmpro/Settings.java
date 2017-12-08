package com.example.asia.jmpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.asia.jmpro.adapters.SettingsFragmentAdapter;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

public class Settings extends MyBaseActivity {
    ViewPager viewPager;
    String[] settingItemTitle;
    PreferencesChangeObserver preferencesChangeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        hideKeyboard(this);
        preferencesChangeObserver = new PreferencesChangeObserver(this).start();
        init();
    }


    public void init(){
        settingItemTitle = getResources().getStringArray(R.array.settings_items);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new SettingsFragmentAdapter(settingItemTitle, getSupportFragmentManager()));
    }

    private void showMainMenuScreen(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showMainMenuScreen();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}


package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.asia.jmpro.adapters.SettingsFragmentAdapter;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

public class Settings extends MyBaseActivity {
    ViewPager viewPager;
    String[] settingItemTitle;
    PreferencesChangeObserver preferencesChangeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        preferencesChangeObserver = new PreferencesChangeObserver(this).start();
    }


    public void init(){
        settingItemTitle = getResources().getStringArray(R.array.settings_items);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SettingsFragmentAdapter(settingItemTitle, getSupportFragmentManager()));
    }

}


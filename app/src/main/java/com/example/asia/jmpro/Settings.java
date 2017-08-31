package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.asia.jmpro.adapters.SettingsFragmentAdapter;

public class Settings extends AppCompatActivity {
    ViewPager viewPager;
    String[] settingItemTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

    }


    public void init(){
        settingItemTitle = getResources().getStringArray(R.array.settings_items);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SettingsFragmentAdapter(settingItemTitle, getSupportFragmentManager()));
    }


}


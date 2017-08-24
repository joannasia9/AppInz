package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.SettingsFragmentAdapter;

public class Settings extends AppCompatActivity {
    ViewPager viewPager;
    String[] settingItemTitle;
    EditText allergenNameEditText;
    ListView settingsMyAllergensListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingItemTitle = getResources().getStringArray(R.array.settings_items);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SettingsFragmentAdapter(settingItemTitle,getSupportFragmentManager()));

        settingsMyAllergensListView = (ListView) findViewById(R.id.settingsMyAllergensListView);
        allergenNameEditText = (EditText) findViewById(R.id.allergenNameEditText);
    }

    public void addAllergen(View view) {

        }
    }


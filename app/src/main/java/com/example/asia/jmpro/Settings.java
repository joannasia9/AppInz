package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.asia.jmpro.adapters.SettingsFragmentAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;

public class Settings extends AppCompatActivity {
    ViewPager viewPager;
    String[] settingItemTitle;
    EditText allergenNameEditText;
    AllergenDao allergenDao = new AllergenDao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

    }

    public void addAllergen(View view) {
        allergenNameEditText = (EditText) findViewById(R.id.allergenNameEditText);
        String name = allergenNameEditText.getText().toString().trim();
        allergenDao.insertAllergenItem(name);
        allergenNameEditText.setText("");
    }


    public void init(){
        settingItemTitle = getResources().getStringArray(R.array.settings_items);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SettingsFragmentAdapter(settingItemTitle, getSupportFragmentManager()));
    }

}


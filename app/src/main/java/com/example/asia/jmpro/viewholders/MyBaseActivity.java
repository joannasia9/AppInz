package com.example.asia.jmpro.viewholders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.asia.jmpro.MyContextWrapper;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.example.asia.jmpro.logic.theme.CurrentThemeHolder;

/**
 * Created by asia on 06/09/2017.
 */

public class MyBaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, newBase.getSharedPreferences("UsersData",MODE_PRIVATE).getString("languageStr","pl")));

    }

}

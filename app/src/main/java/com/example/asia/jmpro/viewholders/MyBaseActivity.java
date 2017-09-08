package com.example.asia.jmpro.viewholders;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.asia.jmpro.MyContextWrapper;

/**
 * Created by asia on 06/09/2017.
 */

public class MyBaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, newBase.getSharedPreferences("UsersData",MODE_PRIVATE).getString("languageStr","pl")));
    }

}

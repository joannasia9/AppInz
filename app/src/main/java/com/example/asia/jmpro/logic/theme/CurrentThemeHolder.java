package com.example.asia.jmpro.logic.theme;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 03/11/2017.
 *
 */

public class CurrentThemeHolder {
    private int mTheme;

    private CurrentThemeHolder() {
        mTheme = R.style.AppTheme;
    }

    private static CurrentThemeHolder instance;

    public static CurrentThemeHolder getInstance() {
        if(instance == null)
            return new CurrentThemeHolder();
        else
            return instance;
    }

    public int getTheme() {
        return mTheme;
    }
    public void setTheme(int newTheme){
        mTheme = newTheme;
    }
}

package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.w("HAHAHAHAHHAHAHA", "onCreateView: FRAGMENT 3 CREATED");
        return inflater.inflate(R.layout.settings_fragment3, container, false);
    }
}

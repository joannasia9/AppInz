package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.AllergensListAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;

import java.util.ArrayList;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment1 extends Fragment {
    ListView settingsMyAllergensListView;
    ArrayList<String> allergens = null;
    AllergenDao allergenDao = new AllergenDao();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.w("HAHAHAHAHHAHAHA", "onCreateView: FRAGMENT 1 CREATED");

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment1,container,false);
        settingsMyAllergensListView = (ListView) fragmentLayout.findViewById(R.id.settingsMyAllergensListView);
        showAllergensList();

        return fragmentLayout;
    }

    public void showAllergensList(){
        allergens = allergenDao.getAllAllergensNames();
        AllergensListAdapter allergenListAdapter = new AllergensListAdapter(getContext(), allergens);
        settingsMyAllergensListView.setAdapter(allergenListAdapter);
    }
}

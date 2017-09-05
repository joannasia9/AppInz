package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.GeneralSettingsListViewAdapter;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment3 extends Fragment {
    ListView gSettingsListView;
    String[] gItemsTitles;
    int[] gItemsImages = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment3, container, false);
        gSettingsListView = (ListView) fragmentLayout.findViewById(R.id.generalSettingsListView);
        gItemsTitles = getResources().getStringArray(R.array.general_settings_items);

       GeneralSettingsListViewAdapter adapter = new GeneralSettingsListViewAdapter(getContext(),gItemsTitles,gItemsImages);
        gSettingsListView.setAdapter(adapter);

        return fragmentLayout;
    }
}

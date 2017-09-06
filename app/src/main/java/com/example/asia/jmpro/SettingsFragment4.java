package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.GeneralSettingsListViewAdapter;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment4 extends Fragment {
    ListView sSettingsListView;
    String[] sItemsTitles;
    int[] sItemsImages = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.settings_fragment4, container, false);

        sSettingsListView = (ListView) fragmentLayout.findViewById(R.id.supportSettingsListView);
        sItemsTitles = getResources().getStringArray(R.array.support_settings_items);

        GeneralSettingsListViewAdapter adapter = new GeneralSettingsListViewAdapter(getContext(),sItemsTitles,sItemsImages);
        sSettingsListView.setAdapter(adapter);

        sSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        break;
                    }

                    case 2: {
                        break;
                    }
                    case 3: {
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        return fragmentLayout;
    }
}

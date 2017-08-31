package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.AllergensListAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.models.Allergen;

import java.util.List;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment1 extends Fragment {
    ListView settingsMyAllergensListView;
    List<Allergen> allergensObjects = null;
    AllergenDao allergenDao = new AllergenDao();
    AllergensListAdapter allergenListAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment1, container, false);
        settingsMyAllergensListView = (ListView) fragmentLayout.findViewById(R.id.settingsMyAllergensListView);

        showAllergensList();

        settingsMyAllergensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Allergen model = allergensObjects.get(position);

                if(model.isSelected()){
                    model.setSelected(false);
                } else {
                    model.setSelected(true);
                }

                allergensObjects.set(position,model);
                allergenListAdapter.updateAdapter(allergensObjects);
            }
        });

        return fragmentLayout;
    }

    public void showAllergensList() {
        allergensObjects = allergenDao.getAllAllergens();

        allergenListAdapter = new AllergensListAdapter(getContext(), allergensObjects);
        settingsMyAllergensListView.setAdapter(allergenListAdapter);

    }
}

package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.AllergensListAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment1 extends Fragment {
    ListView settingsMyAllergensListView;
    Button saveMyAllergens;

    List<Allergen> allergensObjects = null;
    List<String> myAllergens = null;
    AllergenDao allergenDao = new AllergenDao();
    AllergensListAdapter allergenListAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment1, container, false);
        settingsMyAllergensListView = (ListView) fragmentLayout.findViewById(R.id.settingsMyAllergensListView);
        saveMyAllergens = (Button) fragmentLayout.findViewById(R.id.saveMyAllergensButton);

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

        saveMyAllergens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAllergens = getAllCheckedAllergens();
                Toast.makeText(getContext(),"My allergens: "+ myAllergens.toString(),Toast.LENGTH_LONG).show();
                //insert Allergens to userDatabase
            }
        });

        return fragmentLayout;
    }

    public List<String>  getAllCheckedAllergens(){
        List<String> myAllergensList = new ArrayList<>();
        List<Allergen> models = allergenListAdapter.getAllergensList();
        for(Allergen item : models){
            if(item.isSelected()){
                myAllergensList.add(item.getName());
            }
        }
        return myAllergensList;
    }
    public void showAllergensList() {
        allergensObjects = allergenDao.getAllAllergens();

        allergenListAdapter = new AllergensListAdapter(getContext(), allergensObjects);
        settingsMyAllergensListView.setAdapter(allergenListAdapter);

    }
}

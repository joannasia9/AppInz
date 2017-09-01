package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    Button addMyAllergen;
    EditText allergenNameEditText;

    List<Allergen> allAllergensObjects = null;
    List<Allergen> myAllergens = null;
    AllergenDao allergenDao = new AllergenDao();
    AllergensListAdapter allergenListAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment1, container, false);
        settingsMyAllergensListView = (ListView) fragmentLayout.findViewById(R.id.settingsMyAllergensListView);
        saveMyAllergens = (Button) fragmentLayout.findViewById(R.id.saveMyAllergensButton);
        addMyAllergen = (Button) fragmentLayout.findViewById(R.id.addAllergenButton);
        allergenNameEditText = (EditText) fragmentLayout.findViewById(R.id.allergenNameEditText);

        showAllergensList();

        settingsMyAllergensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Allergen model = allAllergensObjects.get(position);

                if(model.isSelected()){
                    model.setSelected(false);
                } else {
                    model.setSelected(true);
                }

                allAllergensObjects.set(position,model);
                allergenListAdapter.updateAdapter(allAllergensObjects);
            }
        });

        saveMyAllergens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAllergens = getAllCheckedAllergens();
                allergenDao.insertMyAllergenList(myAllergens);
                //insert Allergens to userDatabase
            }
        });

        addMyAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAllergen(allergenNameEditText.getText().toString().trim());
            }
        });

        return fragmentLayout;
    }

    public List<Allergen>  getAllCheckedAllergens(){
        List<Allergen> myAllergensList = new ArrayList<>();
        List<Allergen> models = allergenListAdapter.getAllergensList();
        for(Allergen item : models){
            if(item.isSelected()){
                myAllergensList.add(item);
            }
        }
        return myAllergensList;
    }

    public void showAllergensList() {
        allAllergensObjects = allergenDao.getAllAllergens();
        myAllergens = allergenDao.getMyAllergensList();

        if(myAllergens!=null){
            for(Allergen item : myAllergens){
                for(Allergen aItem : allAllergensObjects){
                    if(item.getName().equals(aItem.getName())){
                        aItem.setSelected(true);
                    }

                }
            }
        }

        allergenListAdapter = new AllergensListAdapter(getContext(), allAllergensObjects);
        settingsMyAllergensListView.setAdapter(allergenListAdapter);
    }

    public void addAllergen(String name) {
        allergenDao.insertAllergenItem(name);
        allergenNameEditText.setText("");
        showAllergensList();
    }
}

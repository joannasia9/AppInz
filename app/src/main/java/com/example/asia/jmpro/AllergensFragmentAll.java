package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.adapters.MyAllergenRealmListAdapter;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.db.AllergenDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 10/10/2017.
 *
 */

public class AllergensFragmentAll extends Fragment {
    ListView listView;
    TextView title;
    List<AllergenRealm> allAllergens;
    ArrayList<AllergenRealm> selectedAllergens;
    MyAllergenRealmListAdapter adapter;
    AllergenDao allergenDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allergensFragment = inflater.inflate(R.layout.fragment_allergens_modify,container,false);
        title = (TextView) allergensFragment.findViewById(R.id.textView33);
        title.setText(R.string.all_allergens);

        allergenDao = new AllergenDao();
        allAllergens = allergenDao.getAllAllergenRealm();
        selectedAllergens = new ArrayList<>();

        listView = (ListView) allergensFragment.findViewById(R.id.modifyAllergensListView);
        adapter = new MyAllergenRealmListAdapter(getContext(),allAllergens,selectedAllergens);
        listView.setAdapter(adapter);


        return  allergensFragment;
    }
}

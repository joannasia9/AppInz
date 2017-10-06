package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.adapters.SimpleSubstitutesListAdapter;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.data.db.SubstituteDao;

import java.util.ArrayList;

/**
 * Created by asia on 06/10/2017.
 */

public class SubstitutesFragmentSearch extends Fragment {
    EditText allergenName;
    Button searchButton;
    TextView foundSubstitutesTextView;
    ListView foundSubstitutesListView;
    AllergenDao allergenDao;
    String allergen;
    SimpleSubstitutesListAdapter adapter;
    SubstituteDao substituteDao;
    ArrayList<SubstituteRealm> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View substitutesFragment = inflater.inflate(R.layout.fragment_substitutes_search, container, false);
        allergenName = (EditText) substitutesFragment.findViewById(R.id.searchSubstitutesEditText);
        searchButton = (Button) substitutesFragment.findViewById(R.id.searchSubstitutesButton);
        foundSubstitutesListView = (ListView) substitutesFragment.findViewById(R.id.foundSubstitutesListView);
        foundSubstitutesTextView = (TextView) substitutesFragment.findViewById(R.id.foundSubstitutesTextView);
        allergenDao = new AllergenDao();
        substituteDao = new SubstituteDao(getContext());

        allergenName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(adapter!=null && list.size()!= 0) {
                    list.clear();
                    adapter.updateAdapter(list);
                    foundSubstitutesTextView.setText("");
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allergenName.setError(null);
                if (!isAllergenNameEmpty(allergenName)) {
                    verifyAllergen(allergenName);
                }
            }
        });

        return substitutesFragment;
    }

    private boolean isAllergenNameEmpty(EditText allergenName) {
        if (allergenName.getText().toString().trim().length() == 0) {
            allergenName.setError(getString(R.string.required));
            return true;
        } else return false;
    }

    public void verifyAllergen(EditText allergenName) {
        if (!allergenDao.isAllergenExist(allergenName.getText().toString().trim())) {
            allergenName.setError(getString(R.string.all_does_not_exist));
            allergen = null;
        } else {
            allergen = allergenName.getText().toString().trim();
            allergenName.setText("");
            foundSubstitutesTextView.setText(getResources().getString(R.string.sub_for) + " " + allergen);
            list = substituteDao.getAllAllergensSubstituteList(allergen);
            adapter = new SimpleSubstitutesListAdapter(getContext(), list);
            foundSubstitutesListView.setAdapter(adapter);
        }
    }
}

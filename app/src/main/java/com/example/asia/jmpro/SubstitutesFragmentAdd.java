package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class SubstitutesFragmentAdd extends Fragment {
    EditText allergenName;
    EditText substituteName;
    Button selectAllergen;
    Button addSubstitute;
    ListView substitutesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentAddSubstitutes = inflater.inflate(R.layout.fragment_add_substitutes, container,false);
        allergenName = (EditText) fragmentAddSubstitutes.findViewById(R.id.allergenNameAddSubstitutesEditText);
        substituteName = (EditText) fragmentAddSubstitutes.findViewById(R.id.substituteNameAddSubstitutesEditText);
        selectAllergen = (Button) fragmentAddSubstitutes.findViewById(R.id.setAllergen);
        addSubstitute = (Button) fragmentAddSubstitutes.findViewById(R.id.addSubstitutesButton);
        substitutesList = (ListView) fragmentAddSubstitutes.findViewById(R.id.allergenSubstitutesListView);


        return fragmentAddSubstitutes;
    }
}

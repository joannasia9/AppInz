package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class SubstitutesFragmentSearch extends Fragment {
    EditText allergenName;
    Button searchButton;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View substitutesFragment = inflater.inflate(R.layout.fragment_substitutes_search, container, false);
        allergenName = (EditText) substitutesFragment.findViewById(R.id.searchSubstitutesEditText);
        searchButton = (Button) substitutesFragment.findViewById(R.id.searchSubstitutesButton);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new FoundSubstitutes();
            }
        });

        return substitutesFragment;
    }
}

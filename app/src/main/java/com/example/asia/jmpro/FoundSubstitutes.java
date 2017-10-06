package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class FoundSubstitutes extends Fragment {
    TextView foundSubstitutesTextView;
    ListView foundSubstitutesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.found_substitutes_fragment, container,false);

        foundSubstitutesListView = (ListView) fragment.findViewById(R.id.foundSubstitutesListView);
        foundSubstitutesTextView = (TextView) fragment.findViewById(R.id.foundSubstitutesTextView);

        return fragment;
    }
}

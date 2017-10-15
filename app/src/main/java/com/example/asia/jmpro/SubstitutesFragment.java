package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.MySubstitutesListAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;

/**
 * Created by asia on 06/10/2017.
 *  MY SUBSTITUTES pane
 */

public class SubstitutesFragment extends Fragment {
    ListView listView;
    AllergenDao allergenDao = new AllergenDao();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View substitutesFragment = inflater.inflate(R.layout.fragment_substitutes,container,false);

        listView = (ListView) substitutesFragment.findViewById(R.id.dedicatedSubstitutesListView);

        MySubstitutesListAdapter adapter = new MySubstitutesListAdapter(getContext(), allergenDao.getMyAllergensList());
        listView.setAdapter(adapter);

        return  substitutesFragment;
    }


}

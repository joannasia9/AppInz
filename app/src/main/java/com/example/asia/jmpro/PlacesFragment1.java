package com.example.asia.jmpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by asia on 08/09/2017.
 */

public class PlacesFragment1 extends Fragment{
    TextView placesItem;
    public static final String ITEM_NAME = "itemName";

    public PlacesFragment1(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.places_fragment1,container,false);
        placesItem=(TextView) fragment.findViewById(R.id.placesItem);

        placesItem.setText(getArguments().getString(ITEM_NAME));

        return fragment;
    }
}

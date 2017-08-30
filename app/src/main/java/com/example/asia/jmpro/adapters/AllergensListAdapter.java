package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.viewholders.AllergenItemViewHolder;

import java.util.ArrayList;

/**
 * Created by asia on 30/08/2017.
 */

public class AllergensListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<String> allergensList;

    public AllergensListAdapter(Context context, ArrayList<String> allergens) {
        super(context, R.layout.single_alergen_item);
        this.context = context;
        this.allergensList = allergens;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View allergenItem = convertView;
        AllergenItemViewHolder allergenItemViewHolder;

        if (allergenItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            allergenItem = inflater.inflate(R.layout.single_alergen_item, parent, false);
            allergenItemViewHolder = new AllergenItemViewHolder(allergenItem);
            allergenItem.setTag(allergenItemViewHolder);
        } else {
            allergenItemViewHolder = (AllergenItemViewHolder) allergenItem.getTag();
        }

        allergenItemViewHolder.checkBox.setText(allergensList.get(position));

        return allergenItem;
    }
}

package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.logic.DrawableResourceExtrator;
import com.example.asia.jmpro.models.Allergen;
import com.example.asia.jmpro.viewholders.AllergenItemViewHolder;

import java.util.List;

/**
 * Created by asia on 30/08/2017.
 *
 */

public class AllergensListAdapter extends BaseAdapter{
    private Context context;
    private List<Allergen> allergensList;


    public AllergensListAdapter(Context context, List<Allergen> allergensList) {
        this.context = context;
        this.allergensList = allergensList;
    }

    @Override
    public int getCount() {
        return allergensList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View allergenItem = convertView;
        AllergenItemViewHolder allergenItemViewHolder;

        if (allergenItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            allergenItem = inflater.inflate(R.layout.single_alergen_item, parent, false);

            allergenItemViewHolder = new AllergenItemViewHolder(allergenItem);
            allergenItem.setTag(allergenItemViewHolder);

        } else {
            allergenItemViewHolder = (AllergenItemViewHolder) allergenItem.getTag();
        }

        int selectedResId = DrawableResourceExtrator.getResIdFromAttribute(context, R.attr.selected);
        int notSelectedResId = DrawableResourceExtrator.getResIdFromAttribute(context, R.attr.not_selected);

        Allergen model = allergensList.get(position);
        if(model.isSelected()){
            allergenItemViewHolder.imageView.setImageResource(selectedResId);
        } else {
            allergenItemViewHolder.imageView.setImageResource(notSelectedResId);
        }

        allergenItemViewHolder.textView.setText(model.getName());


        return allergenItem;
    }

    public void updateAdapter(List<Allergen> list){
        this.allergensList = list;
        notifyDataSetChanged();
    }

    public List<Allergen> getAllergensList() {
        return allergensList;
    }
}


package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.viewholders.SuggestedPlaceItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 06/10/2017.
 */

public class SuggestedSubstitutesListAdapter extends BaseAdapter {
    private Context context;
    private List<SubstituteRealm> allSubstitutesList;
    private ArrayList<SubstituteRealm> selectedSubstitutesList;
    private ArrayList<String> selectedSubstitutes;

    public SuggestedSubstitutesListAdapter(Context context, List<SubstituteRealm> allSubstitutesList, ArrayList<SubstituteRealm> suggestedSubstitutesList) {
        this.context = context;
        this.allSubstitutesList = allSubstitutesList;
        this.selectedSubstitutesList = suggestedSubstitutesList;
    }

    @Override
    public int getCount() {
        return allSubstitutesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        selectedSubstitutes = new ArrayList<>();

        for(SubstituteRealm item : selectedSubstitutesList){
            selectedSubstitutes.add(item.getName());
        }

        View item = convertView;

        SuggestedPlaceItemViewHolder viewHolder;

        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            item = inflater.inflate(R.layout.single_suggested_place_item, parent, false);
            viewHolder = new SuggestedPlaceItemViewHolder(item);
            item.setTag(viewHolder);
        } else {
            viewHolder = (SuggestedPlaceItemViewHolder) item.getTag();
        }

        SubstituteRealm model;
        model = allSubstitutesList.get(position);

        viewHolder.suggestedPlaceName.setText(model.getName());


        if (selectedSubstitutesList.size() != 0) {
            if (selectedSubstitutes.contains(model.getName())) {
                viewHolder.toggleButton.setImageResource(R.drawable.recommended);
            } else {
                viewHolder.toggleButton.setImageResource(R.drawable.to_recommend);
            }
        } else {
            viewHolder.toggleButton.setImageResource(R.drawable.to_recommend);
        }

        return item;
    }

    public void updateAdapter(ArrayList<SubstituteRealm> selectedSubstitutesList) {
        this.selectedSubstitutesList = selectedSubstitutesList;
        notifyDataSetChanged();
    }
}

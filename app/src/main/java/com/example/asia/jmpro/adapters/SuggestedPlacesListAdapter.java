package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.data.SuggestedPlace;
import com.example.asia.jmpro.viewholders.SuggestedPlaceItemViewHolder;

import java.util.List;

/**
 * Created by asia on 03/10/2017.
 */

public class SuggestedPlacesListAdapter extends BaseAdapter {
    private Context context;
    private List<Place> favouritePlacesList;
    private List<SuggestedPlace> suggestedPlacesList;
    private Place model;

    public SuggestedPlacesListAdapter (Context c, List<Place> placesList, List<SuggestedPlace> sugPlacesList){
        this.context = c;
        this.favouritePlacesList = placesList;
        this.suggestedPlacesList = sugPlacesList;
    }

    @Override
    public int getCount() {
        return favouritePlacesList.size();
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
        View placeItem = convertView;
        SuggestedPlaceItemViewHolder suggestedPlaceItemViewHolder;

        if(placeItem == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            placeItem = inflater.inflate(R.layout.single_suggested_place_item,parent,false);
            suggestedPlaceItemViewHolder = new SuggestedPlaceItemViewHolder(placeItem);
            placeItem.setTag(suggestedPlaceItemViewHolder);
        } else {
            suggestedPlaceItemViewHolder = (SuggestedPlaceItemViewHolder) placeItem.getTag();
        }

        model = favouritePlacesList.get(position);

        suggestedPlaceItemViewHolder.suggestedPlaceName.setText(model.getName());
        suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.reccomended);

            if(isItemSuggested(model,suggestedPlacesList)){
                suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.reccomend);
            }

        return placeItem;
    }



private boolean isItemSuggested(Place place, List<SuggestedPlace> list) {
    Boolean suggested = false;
    if(list.size()!=0) {
        for (SuggestedPlace item : list) {
            if (item.getName().equals(place.getName())) {
                suggested = true;
                break;
            } else suggested = false;
        }
    }

    return suggested;
    }

    public void updateAdapter (List<SuggestedPlace> list){
        this.suggestedPlacesList = list;
        notifyDataSetChanged();
    }

}


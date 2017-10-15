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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 03/10/2017.
 */

public class SuggestedPlacesListAdapter extends BaseAdapter {
    private Context context;
    private List<Place> favouritePlacesList;
    private List<SuggestedPlace> suggestedPlacesList;
    private ArrayList<Place> selectedPlacesList;
    private int code;
    public static final int SUGGEST_CODE = 10;
    public static final int SELECT_CODE = 11;
    public static final int SELECT_FOR_SHARE_VIA_FACEBOOK_CODE = 12;

    public SuggestedPlacesListAdapter(Context c, List<Place> placesList, List<SuggestedPlace> sugPlacesList, ArrayList<Place> selectedPlacesList, int code) {
        this.context = c;
        this.favouritePlacesList = placesList;
        this.suggestedPlacesList = sugPlacesList;
        this.selectedPlacesList = selectedPlacesList;
        this.code = code;
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

        if (placeItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            placeItem = inflater.inflate(R.layout.single_suggested_place_item, parent, false);
            suggestedPlaceItemViewHolder = new SuggestedPlaceItemViewHolder(placeItem);
            placeItem.setTag(suggestedPlaceItemViewHolder);
        } else {
            suggestedPlaceItemViewHolder = (SuggestedPlaceItemViewHolder) placeItem.getTag();
        }

        Place model;
        model = favouritePlacesList.get(position);

        suggestedPlaceItemViewHolder.suggestedPlaceName.setText(model.getName());


        if (code == SUGGEST_CODE) {
            if (isItemSuggested(model, suggestedPlacesList)) {
                suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.reccomend);
            } else {
                suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.reccomended);
            }
        }

        if (code == SELECT_CODE || code == SELECT_FOR_SHARE_VIA_FACEBOOK_CODE) {
            if (selectedPlacesList.size() != 0) {
                if (selectedPlacesList.contains(model)) {
                    suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.toshare);
                } else {
                    suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.nottoshare);
                }
            } else {
                suggestedPlaceItemViewHolder.toggleButton.setImageResource(R.drawable.nottoshare);
            }
        }

        return placeItem;
    }


    private boolean isItemSuggested(Place place, List<SuggestedPlace> list) {
        Boolean suggested = false;
        if (list.size() != 0) {
            for (SuggestedPlace item : list) {
                if (item.getName().equals(place.getName())) {
                    suggested = true;
                    break;
                } else suggested = false;
            }
        }

        return suggested;
    }

    public void updateAdapter(List<SuggestedPlace> list, ArrayList<Place> placesList) {
        this.suggestedPlacesList = list;
        this.selectedPlacesList = placesList;
        notifyDataSetChanged();
    }

}


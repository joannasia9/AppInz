package com.example.asia.jmpro.viewholders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 03/10/2017.
 *
 */

public class SuggestedPlaceItemViewHolder {
    public ImageView toggleButton;
    public TextView suggestedPlaceName;

    public SuggestedPlaceItemViewHolder(View v){
        toggleButton = (ImageView) v.findViewById(R.id.suggestToggleButton);
        suggestedPlaceName = (TextView) v.findViewById(R.id.suggestedPlaceItemName);
    }
}

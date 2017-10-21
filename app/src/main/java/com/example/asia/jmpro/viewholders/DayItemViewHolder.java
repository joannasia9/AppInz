package com.example.asia.jmpro.viewholders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 16/10/2017.
 */

public class DayItemViewHolder {
    public TextView dayId;
    public ConstraintLayout layout;

    public DayItemViewHolder(View v){
        dayId = (TextView) v.findViewById(R.id.dayIdValue);
        layout = (ConstraintLayout) v.findViewById(R.id.dayLayout);
    }
}

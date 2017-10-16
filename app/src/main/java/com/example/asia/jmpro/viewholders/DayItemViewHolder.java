package com.example.asia.jmpro.viewholders;

import android.view.View;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 16/10/2017.
 */

public class DayItemViewHolder {
    public TextView dayId;

    public DayItemViewHolder(View v){
        dayId = (TextView) v.findViewById(R.id.dayIdValue);
    }
}

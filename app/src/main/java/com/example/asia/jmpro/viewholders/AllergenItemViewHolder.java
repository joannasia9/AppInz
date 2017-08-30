package com.example.asia.jmpro.viewholders;

import android.view.View;
import android.widget.CheckBox;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 30/08/2017.
 *
 */

public class AllergenItemViewHolder {
    public CheckBox checkBox;

    public AllergenItemViewHolder(View v){
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
    }
}

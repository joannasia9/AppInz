package com.example.asia.jmpro.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 05/09/2017.
 */

public class SettingsViewHolder {
    public TextView menuItemTitle;
    public ImageView menuItemImage;

    public SettingsViewHolder(View v) {
        menuItemImage = (ImageView) v.findViewById(R.id.sImageview);
        menuItemTitle = (TextView) v.findViewById(R.id.sTextView);
    }


}

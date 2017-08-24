package com.example.asia.jmpro.viewholders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 23/08/2017.
 *
 */

public class MenuItemViewHolder {
    public TextView menuItemTitle;
    public ImageView menuItemImage;
    public ConstraintLayout singleItemLayout;

    public MenuItemViewHolder(View v){
        menuItemImage = (ImageView) v.findViewById(R.id.menuItemImageView);
        menuItemTitle= (TextView) v.findViewById(R.id.menuItemTitle);
        singleItemLayout =  (ConstraintLayout) v.findViewById(R.id.singleItemLayout);
    }

}

package com.example.asia.jmpro;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by asia on 23/08/2017.
 *
 */

public class MenuItemViewHolder {
    TextView menuItemTitle;
    ImageView menuItemImage;
    ConstraintLayout singleItemLayout;

    MenuItemViewHolder(View v){
        menuItemImage = (ImageView) v.findViewById(R.id.menuItemImageView);
        menuItemTitle= (TextView) v.findViewById(R.id.menuItemTitle);
        singleItemLayout =  (ConstraintLayout) v.findViewById(R.id.singleItemLayout);
    }

}

package com.example.asia.jmpro.viewholders;

import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 30/08/2017.
 *
 */

public class AllergenItemViewHolder {
    public ImageView imageView;
    public TextView textView;

    public AllergenItemViewHolder(View v){
        imageView = (ImageView) v.findViewById(R.id.imageView);
        textView = (TextView) v.findViewById(R.id.aItemName);

    }
}

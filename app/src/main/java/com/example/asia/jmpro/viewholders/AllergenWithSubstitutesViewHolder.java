package com.example.asia.jmpro.viewholders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.example.asia.jmpro.R;

/**
 * Created by asia on 06/10/2017.
 */

public class AllergenWithSubstitutesViewHolder {
    public TextView allergenName;
    public TextView allergensSubstitutes;
    public ConstraintLayout layout;

    public AllergenWithSubstitutesViewHolder(View v){
        allergenName = (TextView) v.findViewById(R.id.allergenNameShowMySubTextView);
        allergensSubstitutes = (TextView) v.findViewById(R.id.textView30);
        layout = (ConstraintLayout) v.findViewById(R.id.singleAllergenSubLayout);
    }

}

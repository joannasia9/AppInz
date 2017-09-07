package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.viewholders.SpinnerItemViewHolder;

/**
 * Created by asia on 07/09/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] subjects;


    public SpinnerAdapter(Context c, String[] subjects) {
        super(c, R.layout.spinner_dropdown_item, R.id.textView17, subjects);
        this.context = c;
        this.subjects = subjects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View spinnerItem = convertView;
        SpinnerItemViewHolder spinnerItemViewHolder;

        if (spinnerItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            spinnerItem = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);

            spinnerItemViewHolder = new SpinnerItemViewHolder(spinnerItem);
            spinnerItem.setTag(spinnerItemViewHolder);
        } else {
            spinnerItemViewHolder = (SpinnerItemViewHolder) spinnerItem.getTag();
        }


        spinnerItemViewHolder.spinnerItemTitle.setText(subjects[position]);

        return spinnerItem;
    }
}

package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.viewholders.DayItemViewHolder;

import java.util.ArrayList;

/**
 * Created by asia on 16/10/2017.
 */

public class DaysListAdapter extends BaseAdapter {
    private ArrayList<Day> daysList;
    private Context context;

    public DaysListAdapter(ArrayList<Day> daysList, Context context) {
        this.daysList = daysList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return daysList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View dayItem = convertView;
        DayItemViewHolder dayItemViewHolder;

        if (dayItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            dayItem = inflater.inflate(R.layout.single_day_item, parent, false);
            dayItemViewHolder = new DayItemViewHolder(dayItem);
            dayItem.setTag(dayItemViewHolder);
        } else {
            dayItemViewHolder = (DayItemViewHolder) dayItem.getTag();
        }

        Day model = daysList.get(position);
        dayItemViewHolder.dayId.setText(model.getId());

        return dayItem;
    }

    public void updateAdapter(ArrayList<Day> daysList){
        this.daysList = daysList;
        notifyDataSetChanged();
    }
}

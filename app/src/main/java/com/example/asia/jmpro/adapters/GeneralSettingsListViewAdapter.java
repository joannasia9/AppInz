package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.viewholders.SettingsViewHolder;

/**
 * Created by asia on 04/09/2017.
 *
 */

public class GeneralSettingsListViewAdapter extends BaseAdapter {
private Context context;
private String[] settingTitlesList;

    public GeneralSettingsListViewAdapter(Context context, String[] settingTitlesList) {
        this.context = context;
        this.settingTitlesList = settingTitlesList;
    }


    @Override
    public int getCount() {
        return settingTitlesList.length;
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
        View settingsItem = convertView;
        SettingsViewHolder settingsItemViewHolder;

        if(settingsItem == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            settingsItem = inflater.inflate(R.layout.single_settings_item,parent,false);
            settingsItemViewHolder = new SettingsViewHolder(settingsItem);
            settingsItem.setTag(settingsItemViewHolder);
        } else {
            settingsItemViewHolder = (SettingsViewHolder) settingsItem.getTag();
        }
        settingsItemViewHolder.menuItemTitle.setText(settingTitlesList[position]);
        return settingsItem;

    }
}

package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.viewholders.MenuItemViewHolder;

/**
 * Created by asia on 04/09/2017.
 */

public class GeneralSettingsListViewAdapter extends BaseAdapter {
private Context context;
private String[] settingTitlesList;
private int[] settingsImagesList;

    public GeneralSettingsListViewAdapter(Context context, String[] settingTitlesList, int[] settingsImagesList) {
        this.context = context;
        this.settingTitlesList = settingTitlesList;
        this.settingsImagesList = settingsImagesList;
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
        MenuItemViewHolder settingsItemViewHolder;

        if(settingsItem == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            settingsItem = inflater.inflate(R.layout.single_main_menu_item,parent,false);
            settingsItemViewHolder = new MenuItemViewHolder(settingsItem);
            settingsItem.setTag(settingsItemViewHolder);
        } else {
            settingsItemViewHolder = (MenuItemViewHolder) settingsItem.getTag();
        }

        settingsItemViewHolder.menuItemImage.setImageResource(settingsImagesList[position]);
        settingsItemViewHolder.menuItemTitle.setText(settingTitlesList[position]);
        return settingsItem;

    }
}

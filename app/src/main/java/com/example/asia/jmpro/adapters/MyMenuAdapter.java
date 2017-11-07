package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.viewholders.MenuItemViewHolder;

/**
 * Created by asia on 23/08/2017.
 *
 */

public class MyMenuAdapter extends ArrayAdapter<String>{
    private Context context;
    private String[] menuItemTitlesArray;
    private int[] menuItemImagesArray;


    public MyMenuAdapter(Context c, String[] mItemTitle, int[] mItemImage){
        super(c, R.layout.single_main_menu_item,R.id.menuItemTitle,mItemTitle);
        this.context = c;
        this.menuItemImagesArray = mItemImage;
        this.menuItemTitlesArray = mItemTitle;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View menuItem = convertView;
        MenuItemViewHolder menuItemViewHolder;

        if(menuItem==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            menuItem = inflater.inflate(R.layout.single_main_menu_item,parent, false);

            menuItemViewHolder = new MenuItemViewHolder(menuItem);
            menuItem.setTag(menuItemViewHolder);
        } else {
            menuItemViewHolder = (MenuItemViewHolder) menuItem.getTag();
        }

        menuItemViewHolder.menuItemImage.setImageResource(menuItemImagesArray[position]);
        menuItemViewHolder.menuItemTitle.setText(menuItemTitlesArray[position]);

        return menuItem;
    }


}

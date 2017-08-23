package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainMenu extends AppCompatActivity {
    ListView mItems;
    String[] mItemsTitles;
    int[] mItemsBackground = {R.color.item1,R.color.item2,R.color.item3,R.color.item4};
    int[] mItemsImages = {R.drawable.item1,R.drawable.item2,R.drawable.item3, R.drawable.item4};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mItems = (ListView) findViewById(R.id.menuItemsListView);
        mItemsTitles = getResources().getStringArray(R.array.main_menu_items);

        MyMenuAdapter myMenuAdapter = new MyMenuAdapter(this,mItemsTitles,mItemsImages,mItemsBackground);
        mItems.setAdapter(myMenuAdapter);
    }

}

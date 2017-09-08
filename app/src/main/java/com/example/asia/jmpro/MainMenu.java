package com.example.asia.jmpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.MyMenuAdapter;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.logic.language.LanguageChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

public class MainMenu extends MyBaseActivity {
    ListView mItems;
    String[] mItemsTitles;
    int[] mItemsBackground = {R.color.item1, R.color.item2, R.color.item3, R.color.item4};
    int[] mItemsImages = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};

    SharedPreferences preferences;
    LanguageChangeObserver languageChangeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        preferences = getApplicationContext().getSharedPreferences("UsersData", MODE_PRIVATE);

        mItems = (ListView) findViewById(R.id.menuItemsListView);
        mItemsTitles = getResources().getStringArray(R.array.main_menu_items);

        MyMenuAdapter myMenuAdapter = new MyMenuAdapter(this, mItemsTitles, mItemsImages, mItemsBackground);
        mItems.setAdapter(myMenuAdapter);
        mItems.setOnItemClickListener(listener);

        languageChangeObserver = new LanguageChangeObserver(this).start();
    }


    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: {
                    Toast.makeText(MainMenu.this, "Position 0: Dziennik", Toast.LENGTH_LONG).show();
                    break;
                }
                case 1: {
                    Toast.makeText(MainMenu.this, "Position 1: Substytuty", Toast.LENGTH_LONG).show();
                    break;
                }

                case 2: {
                    startActivity(new Intent(getApplicationContext(), MainMenuPlaces.class));
                    break;
                }
                case 3: {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DbConnector.getInstance().clearData();
    }

}

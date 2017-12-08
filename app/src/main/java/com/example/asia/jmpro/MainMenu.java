package com.example.asia.jmpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.MyMenuAdapter;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.logic.DrawableResourceExtrator;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.example.asia.jmpro.logic.location.LocationChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

public class MainMenu extends MyBaseActivity{
    ListView mItems;
    String[] mItemsTitles;
    public static final int THEME_REQ_CODE = 100;

    PreferencesChangeObserver preferencesChangeObserver;
    Intent serviceNotificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        preferencesChangeObserver = new PreferencesChangeObserver(this);
        mItems = findViewById(R.id.menuItemsListView);
        mItemsTitles = getResources().getStringArray(R.array.main_menu_items);

        int img1 = DrawableResourceExtrator.getResIdFromAttribute(this, R.attr.my_diary);
        int img2 = DrawableResourceExtrator.getResIdFromAttribute(this, R.attr.allergens);
        int img3 = DrawableResourceExtrator.getResIdFromAttribute(this, R.attr.substitutes);
        int img4 = DrawableResourceExtrator.getResIdFromAttribute(this, R.attr.places);
        int img5 = DrawableResourceExtrator.getResIdFromAttribute(this, R.attr.settings);

        int [] images = {img1, img2, img3, img4, img5};

        MyMenuAdapter myMenuAdapter = new MyMenuAdapter(this, mItemsTitles,images);
        mItems.setAdapter(myMenuAdapter);
        mItems.setOnItemClickListener(listener);

        serviceNotificationIntent = new Intent(this, LocationChangeObserver.class);

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: {
                    startActivity(new Intent(getApplicationContext(), Diary.class));
                    break;
                }
                case 1:
                    startActivity(new Intent(getApplicationContext(),MainMenuAllergens.class));
                    break;
                case 2: {
                    startActivity(new Intent(getApplicationContext(), SubstitutesActivity.class));
                    break;
                }

                case 3: {
                    startActivity( new Intent(getApplicationContext(), MainMenuPlaces.class));
                    break;
                }
                case 4: {
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivityForResult(intent, THEME_REQ_CODE );
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
        stopService(serviceNotificationIntent);
        DbConnector.getInstance().clearData();
        System.exit(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == THEME_REQ_CODE && resultCode == RESULT_OK) {
                recreate();
        }
    }
}

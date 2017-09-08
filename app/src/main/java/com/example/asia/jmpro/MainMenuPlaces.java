package com.example.asia.jmpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainMenuPlaces extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    String[] placesOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_to_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add your location to the favourites places.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        placesOptions = getApplicationContext().getResources().getStringArray(R.array.places_options);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectItem(0);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favourite_places) {
            selectItem(0);
        } else if (id == R.id.nav_suggested_places) {
            selectItem(1);
        } else if (id == R.id.nav_suggest) {
            selectItem(2);
        } else if (id == R.id.nav_share_email) {
            Toast.makeText(this, "Share via email clicked", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share_facebook) {
            Toast.makeText(this, "Share via fb clicked", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share_messenger) {
            Toast.makeText(this, "Share via msn clicked", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void selectItem(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                fragment = new PlacesFragment1();
                args.putString(PlacesFragment1.ITEM_NAME, placesOptions[position]);
                break;
            case 1:
                fragment = new PlacesFragment1();
                args.putString(PlacesFragment1.ITEM_NAME, placesOptions[position]);
                break;
            case 2:
                fragment = new PlacesFragment1();
                args.putString(PlacesFragment1.ITEM_NAME, placesOptions[position]);
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            default:
                break;
        }

        try {
            assert fragment != null;
            fragment.setArguments(args);
        } catch(java.lang.NullPointerException e){
            Log.e("SMUTECZEK", "selectItem: " + e.getMessage() );
        }
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.places_fragment_layout, fragment)
                .commit();


        drawer.closeDrawer(GravityCompat.START);
    }

}

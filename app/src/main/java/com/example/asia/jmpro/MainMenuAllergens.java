package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;

public class MainMenuAllergens extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    PreferencesChangeObserver preferencesChangeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_allergens);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new AllergensFragmentAll();
        replaceFragmentContent(fragment);
        preferencesChangeObserver = new PreferencesChangeObserver(this).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItem(item);
        return true;
    }


    private void selectItem(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.nav_all_allergens:
                fragment = new AllergensFragmentAll();
                break;
            case R.id.nav_add_allergen:
                fragment = new AllergensFragment();
                break;
            case R.id.nav_modify:
                fragment = new AllergensFragmentModify();
                break;
            case R.id.nav_remove_allergen:
                fragment = new AllergensFragmentRemove();
                break;
            case R.id.nav_set_my_allergens:
                fragment = new SettingsFragment1();
                break;
        }
        replaceFragmentContent(fragment);

        setTitle(menuItem.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }

    private void replaceFragmentContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentAllergens, fragment).commit();
    }

}

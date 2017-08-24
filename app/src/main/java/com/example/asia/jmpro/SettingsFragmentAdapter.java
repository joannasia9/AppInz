package com.example.asia.jmpro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by asia on 24/08/2017.
 *
 */

public class SettingsFragmentAdapter extends FragmentPagerAdapter {
    String[] item;

    public SettingsFragmentAdapter (String[] items, FragmentManager fm ) {
        super(fm);
        this.item=items;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position){
            case 0: {
                fragment = new SettingsFragment1();
                break;
            }

            case 1:{
                fragment = new SettingsFragment2();
                break;
            }

            case 2:{
                fragment = new SettingsFragment3();
                break;
            }

            case 3:{
                fragment = new SettingsFragment4();
                break;
            }

            default:{
                fragment = new SettingsFragment1();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }


    public CharSequence getPageTitle(int position) {
        return item[position];
    }

}

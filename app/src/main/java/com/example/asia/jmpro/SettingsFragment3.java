package com.example.asia.jmpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asia.jmpro.adapters.GeneralSettingsListViewAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment3 extends Fragment {
    SharedPreferences prefs;
    static final String PREFERENCES_STRING_KEY = "languageStr";
    static final String PREFERENCES_INT_KEY = "languageId";

    ListView gSettingsListView;
    String[] gItemsTitles;
    int[] gItemsImages = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment3, container, false);
        prefs = getContext().getSharedPreferences("UsersData", MODE_PRIVATE);

        gSettingsListView = (ListView) fragmentLayout.findViewById(R.id.generalSettingsListView);
        gItemsTitles = getResources().getStringArray(R.array.general_settings_items);

        GeneralSettingsListViewAdapter adapter = new GeneralSettingsListViewAdapter(getContext(), gItemsTitles, gItemsImages);
        gSettingsListView.setAdapter(adapter);

        gSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {

                        break;
                    }
                    case 1: {
                        showLanguageSelectorDialog();
                        break;
                    }

                    case 2: {
                        break;
                    }
                    case 3: {
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        return fragmentLayout;
    }

    private void showLanguageSelectorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_language)
                .setSingleChoiceItems(R.array.languages, prefs.getInt(PREFERENCES_INT_KEY, 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREFERENCES_INT_KEY, which);
                        if (which == 0) {
                            editor.putString(PREFERENCES_STRING_KEY, getString(R.string.pl));


                        } else {
                            editor.putString(PREFERENCES_STRING_KEY, getString(R.string.en));
                        }
                        editor.apply();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().recreate();
                        dialog.cancel();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        builder.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(MyContextWrapper.wrap(context, context.getSharedPreferences("UsersData", MODE_PRIVATE).getString(PREFERENCES_STRING_KEY, getString(R.string.pl))));
    }

}



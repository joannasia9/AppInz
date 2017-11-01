package com.example.asia.jmpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.logic.location.LocationChangeObserver;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment3 extends Fragment {
    SharedPreferences prefs;
    static final String PREFERENCES_STRING_KEY = "languageStr";
    static final String PREFERENCES_INT_KEY = "languageId";
    static final String NOTIFICATIONS_ON_OFF_KEY = "notificationsStatus";

    ListView gSettingsListView;
    String[] gItemsTitles;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.settings_fragment3, container, false);
        prefs = getContext().getSharedPreferences("UsersData", MODE_PRIVATE);

        gSettingsListView = (ListView) fragmentLayout.findViewById(R.id.generalSettingsListView);
        gItemsTitles = getResources().getStringArray(R.array.general_settings_items);

        GeneralSettingsListViewAdapter adapter = new GeneralSettingsListViewAdapter(getContext(), gItemsTitles);
        gSettingsListView.setAdapter(adapter);

        gSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        showTurnNotificationsOnDialog();
                        break;
                    }
                    case 1: {
                        showLanguageSelectorDialog();
                        break;
                    }

                    case 2: {
                        //motyw
                        break;
                    }
                    case 3: {
                        showLogoutDialog();
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

    private void showTurnNotificationsOnDialog(){
        String message = "";
        switch (prefs.getInt(NOTIFICATIONS_ON_OFF_KEY,0)){
            case 0:
                message =  getString(R.string.off_not);
                break;
            case 1:
                message = getString(R.string.on_notif);
                break;
        }


        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage(message)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefs.edit();
                        switch (prefs.getInt(NOTIFICATIONS_ON_OFF_KEY,0)){
                            case 0:
                                editor.putInt(NOTIFICATIONS_ON_OFF_KEY, 1);
                                getContext().stopService(new Intent(getContext(), LocationChangeObserver.class));
                                break;
                            case 1:
                                editor.putInt(NOTIFICATIONS_ON_OFF_KEY, 0);
                                getContext().startService(new Intent(getContext(), LocationChangeObserver.class));
                                break;
                            default:
                                editor.putInt(NOTIFICATIONS_ON_OFF_KEY,0);
                                getContext().startService(new Intent(getContext(), LocationChangeObserver.class));
                        }
                        editor.apply();
                    }
                })
                .create();
        dialog.show();


    }

    private void showLogoutDialog(){
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage("Na pewno chcesz się wylogować?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbConnector.getInstance().clearData();
                        startActivity(new Intent(getContext(),MainActivity.class));
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(MyContextWrapper.wrap(context, context.getSharedPreferences("UsersData", MODE_PRIVATE).getString(PREFERENCES_STRING_KEY, getString(R.string.pl))));
    }

}



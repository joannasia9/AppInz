package com.example.asia.jmpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.asia.jmpro.adapters.GeneralSettingsListViewAdapter;
import com.example.asia.jmpro.adapters.SpinnerAdapter;

/**
 * Created by asia on 24/08/2017.
 */

public class SettingsFragment4 extends Fragment {
    ListView sSettingsListView;
    EditText messageEditText;
    Spinner spinner;
    Dialog dialog;
    String selectedSubject;
    String[] sItemsTitles;
    int[] sItemsImages = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.settings_fragment4, container, false);

        sSettingsListView = (ListView) fragmentLayout.findViewById(R.id.supportSettingsListView);
        sItemsTitles = getResources().getStringArray(R.array.support_settings_items);

        GeneralSettingsListViewAdapter adapter = new GeneralSettingsListViewAdapter(getContext(), sItemsTitles, sItemsImages);
        sSettingsListView.setAdapter(adapter);

        sSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        showEmailDialog();
                        break;
                    }
                    case 1: {
                        showAskForDialog();
                        break;
                    }

                    case 2: {
                        share();
                        break;
                    }
                    case 3: {
                        showClearPreferencesDialog();
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        return fragmentLayout;
    }

    public void showAskForDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.single_subject_email_sender);
        dialog.setTitle(getResources().getString(R.string.email_sender));
        dialog.show();

        messageEditText = (EditText) dialog.findViewById(R.id.singleSubSenderEt);
        Button sendButton = (Button) dialog.findViewById(R.id.singleSubEMailSenderB);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSpecialMail();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.button3);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void showEmailDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.email_sender);
        dialog.setTitle(getResources().getString(R.string.email_sender));
        dialog.show();

        spinner = (Spinner) dialog.findViewById(R.id.spinner);
        String[] subjects = getResources().getStringArray(R.array.email_subjects);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), subjects);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSubject = getString(R.string.suggestions);
            }
        });

        messageEditText = (EditText) dialog.findViewById(R.id.messageEditTest);

        Button sendButton = (Button) dialog.findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.emailSenderButton2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void showClearPreferencesDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning))
                .setMessage(R.string.are_you_sure2)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSharedPreferences();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        builder.show();
    }


    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType(getString(R.string.intent_type));
        String shareBody = getString(R.string.offer);
        String shareSub = getString(R.string.write_to) + getString(R.string.app_mail) + getString(R.string.offer2);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse(getString(R.string.mailto)));
        String to[] = {getString(R.string.app_mail)};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, selectedSubject);
        intent.putExtra(Intent.EXTRA_TEXT, messageEditText.getText().toString());

        intent.setType(getString(R.string.intent_type2));

        Intent chooser = Intent.createChooser(intent, getString(R.string.e_mail));
        startActivity(chooser);
        dialog.cancel();
    }

    private void sendSpecialMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse(getString(R.string.mailto)));
        String to[] = {getString(R.string.app_mail)};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.ask_for_new_func));
        intent.putExtra(Intent.EXTRA_TEXT, messageEditText.getText().toString());

        intent.setType(getString(R.string.intent_type2));

        Intent chooser = Intent.createChooser(intent, getString(R.string.e_mail));
        startActivity(chooser);
        dialog.cancel();
    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("UsersData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        getActivity().recreate();
    }


}

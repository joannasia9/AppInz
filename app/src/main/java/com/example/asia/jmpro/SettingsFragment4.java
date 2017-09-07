package com.example.asia.jmpro;

import android.app.Dialog;
import android.content.Intent;
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
    Button sendButton;
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

    public void showAskForDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.single_subject_email_sender);
        dialog.setTitle(getResources().getString(R.string.email_sender));
        dialog.show();

        messageEditText = (EditText) dialog.findViewById(R.id.singleSubSenderEt);
        sendButton = (Button) dialog.findViewById(R.id.singleSubEMailSenderB);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSpecialMail();
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
        sendButton = (Button) dialog.findViewById(R.id.button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String to[] = {"joasia42@interia.eu"};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, selectedSubject);
        intent.putExtra(Intent.EXTRA_TEXT, messageEditText.getText().toString());

        intent.setType("message/rfc822");

        Intent chooser = Intent.createChooser(intent, "Email");
        startActivity(chooser);
        dialog.cancel();
    }

    private void sendSpecialMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String to[] = {"joasia42@interia.eu"};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.ask_for_new_func));
        intent.putExtra(Intent.EXTRA_TEXT, messageEditText.getText().toString());

        intent.setType("message/rfc822");

        Intent chooser = Intent.createChooser(intent, "Email");
        startActivity(chooser);
        dialog.cancel();
    }


}

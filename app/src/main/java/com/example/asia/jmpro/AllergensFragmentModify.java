package com.example.asia.jmpro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.MyAllergenRealmListAdapter;
import com.example.asia.jmpro.adapters.SimpleSubstitutesListAdapter;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.data.db.SubstituteDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 10/10/2017.
 */

public class AllergensFragmentModify extends Fragment {
    String newAllergenName, oldAllergenName;
    ListView listView;
    List<AllergenRealm> allAllergens;
    ArrayList<AllergenRealm> selectedAllergens;
    MyAllergenRealmListAdapter adapter;
    SimpleSubstitutesListAdapter simpleSubstitutesListAdapter;

    ArrayList<SubstituteRealm> substituteRealms;
    SubstituteDao substituteDao;
    AllergenDao allergenDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allergensFragment = inflater.inflate(R.layout.fragment_allergens_modify, container, false);
        allergenDao = new AllergenDao();
        substituteDao = new SubstituteDao(getContext());
        selectedAllergens = new ArrayList<>();


        listView = (ListView) allergensFragment.findViewById(R.id.modifyAllergensListView);
        allAllergens = allergenDao.getAllAllergenRealm();
        adapter = new MyAllergenRealmListAdapter(getContext(), allAllergens, selectedAllergens);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showModifyAllergenDialog(position);
            }
        });

        return allergensFragment;
    }

    private void showModifyAllergenDialog(int position) {
        final AllergenRealm allergenRealm = allAllergens.get(position); // allergenRealm

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.allergen_modify_dialog);
        final EditText allergenName = (EditText) dialog.findViewById(R.id.allergenModifiedName);
        final EditText substituteName = (EditText) dialog.findViewById(R.id.substituteModifiedName);

        ListView listView = (ListView) dialog.findViewById(R.id.substitutesModifyListView);

        substituteRealms = substituteDao.getAllAllergensSubstituteList(allergenRealm.getAllergenName());
        simpleSubstitutesListAdapter = new SimpleSubstitutesListAdapter(getContext(), substituteRealms);

        listView.setAdapter(simpleSubstitutesListAdapter);

        newAllergenName = allergenRealm.getAllergenName();
        oldAllergenName = allergenRealm.getAllergenName();
        allergenName.setText(newAllergenName);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeletionDialog(position);
            }
        });

        Button addToListButton = (Button) dialog.findViewById(R.id.button5);
        addToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (substituteName.getText().toString().trim().length() != 0) {
                    if(convertToArrayString(substituteRealms).contains(getString(R.string.no_result))){
                        substituteRealms.remove(searchIndex(getString(R.string.no_result),substituteRealms));
                    }
                    SubstituteRealm substituteRealm = new SubstituteRealm(substituteName.getText().toString().trim());
                    substituteRealms.add(substituteRealm);
                    simpleSubstitutesListAdapter.updateAdapter(substituteRealms);
                    substituteName.setText("");
                }
            }
        });

        Button saveButton = (Button) dialog.findViewById(R.id.button6);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allergenName.getText().toString().trim().length() != 0) {
                    newAllergenName = allergenName.getText().toString().trim();
                    showQuestionAlertDialog(oldAllergenName, newAllergenName, substituteRealms);
                    dialog.cancel();
                } else allergenName.setError(getString(R.string.required));
            }
        });

        dialog.create();
        dialog.show();
    }

    private void showDeletionDialog(final int position) {
        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage(R.string.if_definitely)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        substituteRealms.remove(position);
                        simpleSubstitutesListAdapter.updateAdapter(substituteRealms);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        builder.show();
    }

    private void showQuestionAlertDialog(final String oldAllergenName, final String newAllergenName, final ArrayList<SubstituteRealm> substituteRealms) {
        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage(R.string.save_changes)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        allergenDao.updateAllergenRealm(oldAllergenName, newAllergenName, substituteRealms);
                        allAllergens = allergenDao.getAllAllergenRealm();
                        adapter.updateAdapter(allAllergens,selectedAllergens);
                        Toast.makeText(getContext(), getContext().getString(R.string.success), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        builder.show();
    }

    private ArrayList<String> convertToArrayString(ArrayList<SubstituteRealm> oldList){
        ArrayList<String> newList = new ArrayList<>(oldList.size());
        for(SubstituteRealm item : oldList){
            newList.add(item.getName());
        }
        return newList;
    }

    private int searchIndex(String elementName, ArrayList<SubstituteRealm> list){
        int index = 0;
        for(int i = 0; i< list.size(); i++){
            if(list.get(i).getName().equals(elementName)){
                index = i;
            }
        }
        return index;
    }
}

package com.example.asia.jmpro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.MyAllergenRealmListAdapter;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.AllergenString;
import com.example.asia.jmpro.data.db.AllergenDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 10/10/2017.
 *
 */

public class AllergensFragmentRemove extends Fragment {
    ListView listView;
    List<AllergenRealm> allAllergens;
    ArrayList<String> selectedAllergens;
    MyAllergenRealmListAdapter adapter;
    AllergenDao allergenDao;
    TextView title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allergensFragment = inflater.inflate(R.layout.fragment_allergens_modify, container, false);
        title = allergensFragment.findViewById(R.id.textView33);
        title.setText(R.string.remove_allergens_from_db);

        allergenDao = new AllergenDao();
        allAllergens = allergenDao.getAllAllergenRealm();
        selectedAllergens = new ArrayList<>();

        listView = allergensFragment.findViewById(R.id.modifyAllergensListView);
        adapter = new MyAllergenRealmListAdapter(getContext(), allAllergens, selectedAllergens);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AllergenRealm allergenRealm = allAllergens.get(position);
                showRemoveAllergenAlertDialog(allergenRealm.getAllergenName());
            }


        });

        return allergensFragment;
    }

    private void showRemoveAllergenAlertDialog(final String allergenName) {
        final ArrayList<String> addedByUser = new ArrayList<>();
        final ArrayList<AllergenRealm> list = new ArrayList<>();
        ArrayList<AllergenString> allergensStringList = allergenDao.getAllAllergensStringAddedByMe();

        for (AllergenString item : allergensStringList) {
            addedByUser.add(item.getName());
        }


    if(getContext()!=null) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.if_u_sure) + " " + allergenName + " " + getString(R.string.from_db))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addedByUser.contains(allergenName)) {
                            allergenDao.deleteAllergenFromGlobalDb(allergenName);
                            allergenDao.deleteAllergenFromPrivateDb(allergenName);
                            addedByUser.remove(allergenName);
                            Toast.makeText(getContext(), getString(R.string.removed_suc), Toast.LENGTH_LONG).show();
                        } else {
                            showAllertDialog();
                        }

                        allAllergens = allergenDao.getAllAllergenRealm();
                        list.addAll(allAllergens);

                        adapter.updateAdapter(list, new ArrayList<String>());
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
    }

    private void showAllertDialog() {
        if (getContext() != null) {
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.remove_only_added_by_yourself))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            builder.show();
        }
    }
}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.AllergensListAdapter;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;

/**
 * Created by asia on 10/10/2017.
 *
 */

public class AllergensFragment extends Fragment {
    AllergenDao allergenDao;
    ArrayList<Allergen> allergensToAdd;
    ListView listView;
    Button addToListButton;
    Button addToDbButton;
    EditText allergenName;
    AllergensListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allergensFragment = inflater.inflate(R.layout.fragment_add_allergens,container,false);

        addToDbButton = allergensFragment.findViewById(R.id.addToDbButton);
        addToListButton = allergensFragment.findViewById(R.id.addToListButton);
        allergenName = allergensFragment.findViewById(R.id.allergenToAdd);

        listView =  allergensFragment.findViewById(R.id.allergensAddAllergensListView);

        allergenDao = new AllergenDao();

        allergensToAdd = new ArrayList<>();

        adapter = new AllergensListAdapter(getContext(),allergensToAdd);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showIfSureDialog(position);
            }
        });

        addToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergenName.getText().toString().trim().length() != 0){
                    if(allergenDao.isAllergenExist(allergenName.getText().toString().trim())){
                        allergenName.setError(getString(R.string.allergen_exists));
                    } else {
                        allergensToAdd.add(new Allergen(allergenName.getText().toString().trim(),false));
                        allergenName.setText("");
                        adapter.updateAdapter(allergensToAdd);
                    }
                } else allergenName.setError(getString(R.string.required));
            }
        });

        addToDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergensToAdd.size()!= 0){
                    showQuestionDialog(allergensToAdd);
                } else if(allergenName.getText().toString().trim().length() != 0) {
                    Allergen allergen = new Allergen(allergenName.getText().toString().trim(),false);
                    allergensToAdd.add(allergen);
                    showQuestionDialog(allergensToAdd);
                } else {
                    showNothingToAddAlertDialog();
                }
            }
        });

        return  allergensFragment;
    }

    private void showIfSureDialog(final int position) {
        if(getContext()!=null) {
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.warning))
                    .setMessage("Czy na pewno chcesz usunąć element z listy?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allergensToAdd.remove(position);
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
    }

    private void showNothingToAddAlertDialog() {
        if(getContext()!=null) {
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setMessage(R.string.nothing_to_add_warning)
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

    private void showQuestionDialog(final ArrayList<Allergen> allergensToAdd) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("\n");
        for (Allergen item : allergensToAdd) {
            buffer.append(item.getName()).append("\n");
        }
        if (getContext() != null){
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.u_sure) + buffer.toString() + getString(R.string.to_db))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allergenDao = new AllergenDao();
                            for (Allergen item : allergensToAdd) {
                                allergenDao.insertAllergenItemToTheGlobalDB(item.getName());
                                allergenDao.insertAllergenItemToThePrivateDB(item.getName());
                            }
                            Toast.makeText(getContext(), getString(R.string.added_suc_allergene) + buffer.toString(), Toast.LENGTH_LONG).show();
                            allergensToAdd.clear();
                            adapter.updateAdapter(allergensToAdd);
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
    }
}

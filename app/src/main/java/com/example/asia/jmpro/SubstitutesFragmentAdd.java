package com.example.asia.jmpro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.adapters.SimpleSubstitutesListAdapter;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.data.db.SubstituteDao;

import java.util.ArrayList;

/**
 * Created by asia on 06/10/2017.
 */

public class SubstitutesFragmentAdd extends Fragment {
    TextView substitutesOf;
    EditText allergenName;
    EditText substituteName;
    Button selectAllergen;
    Button addSubstitute;
    ListView substitutesList;
    ArrayList<SubstituteRealm> allAllergenSubstitutesList;
    SubstituteDao substituteDao;
    AllergenDao allergenDao;
    SimpleSubstitutesListAdapter adapter;
    String allergen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentAddSubstitutes = inflater.inflate(R.layout.fragment_add_substitutes, container, false);
        substituteDao = new SubstituteDao(getContext());
        allergenName = (EditText) fragmentAddSubstitutes.findViewById(R.id.allergenNameAddSubstitutesEditText);
        substituteName = (EditText) fragmentAddSubstitutes.findViewById(R.id.substituteNameAddSubstitutesEditText);
        selectAllergen = (Button) fragmentAddSubstitutes.findViewById(R.id.setAllergen);
        addSubstitute = (Button) fragmentAddSubstitutes.findViewById(R.id.addSubstitutesButton);
        substitutesList = (ListView) fragmentAddSubstitutes.findViewById(R.id.allergenSubstitutesListView);
        substitutesOf = (TextView) fragmentAddSubstitutes.findViewById(R.id.substitutesOf);

        allAllergenSubstitutesList = new ArrayList<>();

        selectAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allergenName.setError(null);
                substituteName.setEnabled(true);
                if (!isAllergenNameEmpty(allergenName)) {
                    verifyAllergen(allergenName);
                }
            }
        });

        addSubstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSubstituteNameEmpty(substituteName)) {
                    substituteDao.addSubstituteToDatabase(substituteName.getText().toString(), allergenName.getText().toString());

                    allAllergenSubstitutesList = substituteDao.getAllAllergensSubstituteList(allergenName.getText().toString());
                    adapter.updateAdapter(allAllergenSubstitutesList);
                    substituteName.setText("");
                }
            }
        });

        substituteName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(allergen == null) {
                    substituteName.setEnabled(false);
                    allergenName.requestFocus();
                    if (!isAllergenNameEmpty(allergenName)) {
                        allergenName.setError(getString(R.string.press_button));
                    }
                }
                return false;
            }
        });

        allergenName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                allergen = null;
                return false;
            }
        });

        substitutesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showIfDeleteSubstituteDialog(position);
            }
        });

        return fragmentAddSubstitutes;
    }

    private void showIfDeleteSubstituteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getResources().getString(R.string.warning))
                .setMessage(R.string.if_definitely)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SubstituteRealm model = allAllergenSubstitutesList.get(position);
                        substituteDao.removeSubstituteFromDatabase(allergen, model.getName(), position);
                        allAllergenSubstitutesList = substituteDao.getAllAllergensSubstituteList(allergen);
                        adapter.updateAdapter(allAllergenSubstitutesList);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .create();
        builder.show();
    }

    private boolean isAllergenNameEmpty(EditText allergenName) {
        if (allergenName.getText().toString().trim().length() == 0) {
            allergenName.setError(getString(R.string.required));
            return true;
        } else return false;
    }

    private boolean isSubstituteNameEmpty(EditText substituteName) {
        if (substituteName.getText().toString().trim().length() == 0) {
            substituteName.setError(getString(R.string.required));
            return true;
        } else return false;
    }

    private void verifyAllergen(EditText allergenName) {
        allergenDao = new AllergenDao();
        if (!allergenDao.isAllergenExist(allergenName.getText().toString().trim())) {
            allergenName.setError(getString(R.string.all_does_not_exist));
            allergen = null;
        } else {
            allergen = allergenName.getText().toString().trim();
            substituteName.requestFocus();
            String text = getString(R.string.substitutes_of) + " " + allergen;
            substitutesOf.setText(text);

            allAllergenSubstitutesList = new ArrayList<>();
            allAllergenSubstitutesList = substituteDao.getAllAllergensSubstituteList(allergen);
            adapter = new SimpleSubstitutesListAdapter(getContext(), allAllergenSubstitutesList);
            substitutesList.setAdapter(adapter);
        }
    }
}

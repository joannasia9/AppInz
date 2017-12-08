package com.example.asia.jmpro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.AllergensListAdapter;
import com.example.asia.jmpro.adapters.SimpleSubstitutesListAdapter;
import com.example.asia.jmpro.data.AllergenString;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.data.db.SubstituteDao;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;

import static com.example.asia.jmpro.R.id.saveAllAllsButton;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class SubstitutesFragmentAdd extends Fragment{
    TextView substitutesOf;
    EditText allergenName;
    EditText substituteName;
    Button selectAllergen;
    Button addSubstitute;
    ListView substitutesList;
    ArrayList<SubstituteRealm> allAllergenSubstitutesList;
    ArrayList<AllergenString> allergensStringObjects;
    ArrayList<String> allergens;
    SubstituteDao substituteDao;
    AllergenDao allergenDao;
    SimpleSubstitutesListAdapter adapter;
    AllergensListAdapter listAdapter;
    String allergen;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentAddSubstitutes = inflater.inflate(R.layout.fragment_add_substitutes, container, false);
        substituteDao = new SubstituteDao(getContext());
        allergenName = fragmentAddSubstitutes.findViewById(R.id.allergenNameAddSubstitutesEditText);
        substituteName = fragmentAddSubstitutes.findViewById(R.id.substituteNameAddSubstitutesEditText);
        selectAllergen = fragmentAddSubstitutes.findViewById(R.id.setAllergen);
        addSubstitute = fragmentAddSubstitutes.findViewById(R.id.addSubstitutesButton);
        substitutesList = fragmentAddSubstitutes.findViewById(R.id.allergenSubstitutesListView);
        substitutesOf = fragmentAddSubstitutes.findViewById(R.id.substitutesOf);

        allAllergenSubstitutesList = new ArrayList<>();
        allergens = new ArrayList<>();
        allergensStringObjects = new ArrayList<>();

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
        if(getContext() != null) {
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
            showQuestionDialog(allergenName);
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

    private void showQuestionDialog(final EditText allergenName) {
        if(getContext() != null) {
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.warning)
                    .setMessage(getString(R.string.all_does_not_exist) + "\n" + getString(R.string.questin_if_want_add_new))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAddAllergenDialog(allergenName, substituteName);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allergenName.setError(getString(R.string.all_does_not_exist));
                            dialog.cancel();
                        }
                    })
                    .create();
            builder.show();
        }
    }

    private void showAskIfSureDialog(int position) {
                            final AllergenString model = allergensStringObjects.get(position);
                            final AllergenDao allergenDao = new AllergenDao();

                            if(getContext() != null) {
                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setTitle(getString(R.string.warning))
                                        .setMessage(getString(R.string.if_u_sure) + " " + model.getName() + " " + getString(R.string.from_db))
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                allergenDao.deleteAllergenFromGlobalDb(model.getName());
                                                allergenDao.deleteAllergenFromPrivateDb(model.getName());
                                                allergens.remove(model.getName());
                                                allergensStringObjects = allergenDao.getAllAllergensStringAddedByMe();
                                                listAdapter.updateAdapter(convertAllergenStringToAllergenList(allergensStringObjects));
                                                Toast.makeText(getContext(), getString(R.string.removed) + " " + model.getName(), Toast.LENGTH_LONG).show();

                                                dialog.cancel();
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
    private void showAddAllergenDialog(final EditText allergenName, final EditText substituteName) {
        final AllergenDao allergenDao = new AllergenDao();
        allergens = new ArrayList<>();
        allergensStringObjects = allergenDao.getAllAllergensStringAddedByMe();

        if(getContext() != null) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.add_allergen_dialog);

            final EditText allergen = dialog.findViewById(R.id.addAllEditText);
            Button save = dialog.findViewById(saveAllAllsButton);
            final Button cancel = dialog.findViewById(R.id.cancelAllButton);
            final Button add = dialog.findViewById(R.id.addAllButton);

            ListView listView = dialog.findViewById(R.id.allAllsList);
            listAdapter = new AllergensListAdapter(getContext(), convertAllergenStringToAllergenList(allergensStringObjects));
            listView.setAdapter(listAdapter);

            allergen.setText(allergenName.getText().toString());

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showAskIfSureDialog(position);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allergen.getText().toString().trim().length() != 0) {
                        allergens.add(allergen.getText().toString().trim());
                        allergenDao.insertAllergenItemToTheGlobalDB(allergen.getText().toString().trim());
                        allergenDao.insertAllergenItemToThePrivateDB(allergen.getText().toString().trim());
                        allergen.setText("");
                        allergensStringObjects = allergenDao.getAllAllergensStringAddedByMe();
                        listAdapter.updateAdapter(convertAllergenStringToAllergenList(allergensStringObjects));
                    } else {
                        allergen.setError(getString(R.string.required));
                    }
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder buffer = new StringBuilder();

                    if (allergens.size() != 0) {
                        for (String item : allergens) {
                            buffer.append(item).append("\n");
                        }

                        Toast.makeText(getContext(), getString(R.string.added_suc_allergene) + " \n" + buffer.toString(), Toast.LENGTH_LONG).show();
                    }

                    substituteName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    adapter = new SimpleSubstitutesListAdapter(getContext(), allAllergenSubstitutesList);
                    dialog.cancel();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allergenName.setError(getString(R.string.all_does_not_exist));
                    dialog.cancel();
                }
            });

            dialog.show();
        }
    }

    private ArrayList<Allergen> convertAllergenStringToAllergenList(ArrayList<AllergenString> list){
        ArrayList<Allergen> allergensList = new ArrayList<>(list.size());
        for(AllergenString item : list){
            Allergen allergen = new Allergen(item.getName(),false);
            allergensList.add(allergen);
        }
        return allergensList;
    }
}

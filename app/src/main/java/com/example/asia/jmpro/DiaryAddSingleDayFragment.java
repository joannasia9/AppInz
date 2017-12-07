package com.example.asia.jmpro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.UniversalSimpleListAdapter;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.data.db.DayDao;
import com.example.asia.jmpro.logic.calendar.DateUtilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.asia.jmpro.R.id.addEatenProductsButton;

/**
 * Created by asia on 15/10/2017.
 */

public class DiaryAddSingleDayFragment extends Fragment {
    public TextView dateToAdd;
    Button changeDatePickerButton;
    Button saveDayToDbButton;
    Button addRemoveEatenProductsButton;
    Button addRemoveMedicinesButton;
    Button addRemoveSymptomsButton;
    Button addRemoveNoteButton;
    TextView eatenProductsListTV, medicinesTV, symptomsTV, notesTV, pageTitle;
    UniversalSimpleListAdapter productsAdapter, medicinesAdapter, symptomsAdapter, notesAdapter;
    ArrayList<String> selectedProducts, selectedMedicines, selectedSymptoms, selectedNotes;
    DayDao dayDao;
    ArrayList<Product> productsList;
    ArrayList<Medicine> medicinesList;
    ArrayList<Symptom> symptomsList;
    ArrayList<String> addedNotesList;

    Date dateToAddDate;
    Date currentDate;
    View diaryFragment;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        diaryFragment = inflater.inflate(R.layout.diary_add_single_day, container, false);
        dayDao = new DayDao(getContext());

        pageTitle = (TextView) diaryFragment.findViewById(R.id.textView37);

        dateToAdd = (TextView) diaryFragment.findViewById(R.id.dateToAdd);
        String currentDateString = DateUtilities.currentDay() + "." + (DateUtilities.currentMonth() + 1) + "." + DateUtilities.currentYear();
        dateToAdd.setText(currentDateString);

        currentDate = DateUtilities.getDate(DateUtilities.currentYear(), DateUtilities.currentMonth(), DateUtilities.currentDay());

        changeDatePickerButton = (Button) diaryFragment.findViewById(R.id.changeDateButton);
        saveDayToDbButton = (Button) diaryFragment.findViewById(R.id.saveDayToDbButton);

        addRemoveEatenProductsButton = (Button) diaryFragment.findViewById(addEatenProductsButton);
        addRemoveMedicinesButton = (Button) diaryFragment.findViewById(R.id.addMedicinesButton);
        addRemoveSymptomsButton = (Button) diaryFragment.findViewById(R.id.addSymptomButton);
        addRemoveNoteButton = (Button) diaryFragment.findViewById(R.id.addNoteButton);

        eatenProductsListTV = (TextView) diaryFragment.findViewById(R.id.eatenProductsListTV);
        medicinesTV = (TextView) diaryFragment.findViewById(R.id.medicinesTV);
        symptomsTV = (TextView) diaryFragment.findViewById(R.id.symptomsTV);
        notesTV = (TextView) diaryFragment.findViewById(R.id.notesTV);

        setAllArrayListsAndFields(getContext(), currentDate);
        bundle = null;

        addRemoveEatenProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEatenProductDialog();
            }
        });
        addRemoveMedicinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMedicineDialog();
            }
        });
        addRemoveSymptomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSymptomDialog();
            }
        });
        addRemoveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        saveDayToDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog(dateToAddDate);
            }
        });

        changeDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogOnClick();
            }
        });
        return diaryFragment;
    }

    private void showQuestionDialog(final Date date) {

        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.u_sure_save) + " " + convertDateTVToString(date) + " " + getString(R.string.in_db))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dayDao.insertSingleDayToDatabase(date, selectedProducts, selectedMedicines, selectedSymptoms, selectedNotes);
                        setAllArrayListsAndFields(getContext(), currentDate);
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
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

    public void setAllArrayListsAndFields(Context context, Date date) {
        dayDao = new DayDao(context);

        bundle = this.getArguments();
        if (bundle != null) {
            String dateString = bundle.getString("date", convertDateToString(currentDate));
            DateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            try {
                dateToAddDate = simpleDate.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                dateToAddDate = currentDate;
            }

            pageTitle.setText(context.getString(R.string.modify));
            dateToAdd.setText(convertDateTVToString(dateToAddDate));
            selectedProducts = dayDao.getUsersSelectedProductsFromDb(dateString);
            selectedMedicines = dayDao.getUsersSelectedMedicinesFromDb(dateString);
            selectedSymptoms = dayDao.getUsersSelectedSymptomsFromDb(dateString);
            selectedNotes = dayDao.getUsersSelectedNotesFromDb(dateString);
            addedNotesList = new ArrayList<>(selectedNotes.size());
            addedNotesList = selectedNotes;

            eatenProductsListTV.setText(convertFromArrayListOfStringToString(selectedProducts));
            medicinesTV.setText(convertFromArrayListOfStringToString(selectedMedicines));
            symptomsTV.setText(convertFromArrayListOfStringToString(selectedSymptoms));
            notesTV.setText(convertFromArrayListOfStringToString(selectedNotes));

        } else {
            pageTitle.setText(context.getString(R.string.add_day));
            dateToAddDate = date;
            addedNotesList = new ArrayList<>();
            addedNotesList.add(context.getString(R.string.completely_nothing));
            selectedProducts = dayDao.getUsersSelectedProductsFromDb(convertDateToString(date));
            selectedMedicines = dayDao.getUsersSelectedMedicinesFromDb(convertDateToString(date));
            selectedSymptoms = dayDao.getUsersSelectedSymptomsFromDb(convertDateToString(date));
            selectedNotes = dayDao.getUsersSelectedNotesFromDb(convertDateToString(date));

            dateToAdd.setText(convertDateTVToString(date));
            eatenProductsListTV.setText(convertFromArrayListOfStringToString(selectedProducts));
            medicinesTV.setText(convertFromArrayListOfStringToString(selectedMedicines));
            symptomsTV.setText(convertFromArrayListOfStringToString(selectedSymptoms));
            notesTV.setText(convertFromArrayListOfStringToString(selectedNotes));
        }


    }

    public void showDatePickerDialogOnClick() {
        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerListener,
                DateUtilities.currentYear(),
                DateUtilities.currentMonth(),
                DateUtilities.currentDay()
        );
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int birthYear, int monthOfAYear, int dayOfMonth) {
            dateToAdd.setText(dayOfMonth + "." + (monthOfAYear + 1) + "." + birthYear);
            dateToAdd.setTextColor(getResources().getColor(R.color.colorBlack, null));
            dateToAddDate = DateUtilities.getDate(birthYear, monthOfAYear, dayOfMonth);

            setAllArrayListsAndFields(getContext(), dateToAddDate);
        }
    };

    private void showAddEatenProductDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.universal_simple_content);

        final EditText elementName = (EditText) dialog.findViewById(R.id.editText);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getString(R.string.select_products));

        ListView listView = (ListView) dialog.findViewById(R.id.listView);

        productsList = dayDao.getAllProductsArrayList();

        productsAdapter = new UniversalSimpleListAdapter(getContext(), UniversalSimpleListAdapter.REQUEST_CODE_PRODUCTS);
        productsAdapter.setListOfProducts(productsList);
        productsAdapter.setSelectedItems(selectedProducts);

        listView.setAdapter(productsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product model = productsList.get(position);

                if (!selectedProducts.contains(model.getName())) {
                    selectedProducts.add(model.getName());
                } else selectedProducts.remove(model.getName());

                productsList = dayDao.getAllProductsArrayList();
                productsAdapter.updateProductsAdapter(productsList, selectedProducts);
            }
        });

        Button button = (Button) dialog.findViewById(R.id.multiTaskButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedProducts.size() > 1) {
                    selectedProducts.remove(getString(R.string.completely_nothing));
                }
                eatenProductsListTV.setText(convertFromArrayListOfStringToString(selectedProducts));
                dialog.cancel();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.button12);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button addToDb = (Button) dialog.findViewById(R.id.button8);
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elementName.getText().toString().trim().length() != 0) {
                    dayDao.addSingleProductToDb(elementName.getText().toString().trim());
                    selectedProducts.add(elementName.getText().toString().trim());
                    elementName.setText("");
                    Toast.makeText(getContext(), getString(R.string.success_a), Toast.LENGTH_LONG).show();
                } else {
                    elementName.setError(getString(R.string.required));
                }
                productsList = dayDao.getAllProductsArrayList();
                productsAdapter.updateProductsAdapter(productsList, selectedProducts);
            }
        });

        dialog.create();
        dialog.show();


    }

    private void showAddMedicineDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.universal_simple_content);
        dialog.create();

        final EditText elementName = (EditText) dialog.findViewById(R.id.editText);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getString(R.string.select_medicines));

        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        Button button = (Button) dialog.findViewById(R.id.multiTaskButton);
        Button addToDb = (Button) dialog.findViewById(R.id.button8);

        Button cancelButton = (Button) dialog.findViewById(R.id.button12);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        medicinesList = dayDao.getAllMedicinesArrayList();
        medicinesAdapter = new UniversalSimpleListAdapter(getContext(), UniversalSimpleListAdapter.REQUEST_CODE_MEDICINES);
        medicinesAdapter.setListOfMedicines(medicinesList);
        medicinesAdapter.setSelectedItems(selectedMedicines);
        listView.setAdapter(medicinesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine model = medicinesList.get(position);
                if (!selectedMedicines.contains(model.getName())) {
                    selectedMedicines.add(model.getName());
                } else selectedMedicines.remove(model.getName());

                medicinesList = dayDao.getAllMedicinesArrayList();
                medicinesAdapter.updateMedicinesAdapter(medicinesList, selectedMedicines);
            }
        });

        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elementName.getText().toString().trim().length() != 0) {
                    dayDao.addSingleMedicineToDb(elementName.getText().toString().trim());
                    selectedMedicines.add(elementName.getText().toString().trim());
                    elementName.setText("");
                    Toast.makeText(getContext(), getString(R.string.success_a), Toast.LENGTH_LONG).show();
                } else {
                    elementName.setError(getString(R.string.required));
                }

                medicinesList = dayDao.getAllMedicinesArrayList();
                medicinesAdapter.updateMedicinesAdapter(medicinesList, selectedProducts);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMedicines.size() > 1 && selectedMedicines.contains(getString(R.string.completely_nothing))) {
                    selectedMedicines.remove(getString(R.string.completely_nothing));
                }

                medicinesTV.setText(convertFromArrayListOfStringToString(selectedMedicines));
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void showAddSymptomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.universal_simple_content);
        dialog.create();

        final EditText elementName = (EditText) dialog.findViewById(R.id.editText);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getString(R.string.select_symptoms));

        symptomsList = dayDao.getAllSymptomsArrayList();
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        symptomsAdapter = new UniversalSimpleListAdapter(getContext(), UniversalSimpleListAdapter.REQUEST_CODE_SYMPTOMS);
        symptomsAdapter.setListOfSymptoms(symptomsList);
        symptomsAdapter.setSelectedItems(selectedSymptoms);
        listView.setAdapter(symptomsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Symptom model = symptomsList.get(position);
                if (!selectedSymptoms.contains(model.getName())) {
                    selectedSymptoms.add(model.getName());
                } else selectedSymptoms.remove(model.getName());

                symptomsList = dayDao.getAllSymptomsArrayList();
                symptomsAdapter.updateSymptomsAdapter(symptomsList, selectedSymptoms);
            }
        });

        Button button = (Button) dialog.findViewById(R.id.multiTaskButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSymptoms.contains(getString(R.string.completely_nothing))) {
                    selectedSymptoms.remove(getString(R.string.completely_nothing));
                }

                if (selectedSymptoms.size() == 0) {
                    selectedSymptoms.add(getString(R.string.completely_nothing));
                }

                symptomsTV.setText(convertFromArrayListOfStringToString(selectedSymptoms));
                dialog.cancel();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.button12);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button addToDb = (Button) dialog.findViewById(R.id.button8);
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elementName.getText().toString().trim().length() != 0) {
                    dayDao.addSingleSymptomToDb(elementName.getText().toString().trim());
                    selectedSymptoms.add(elementName.getText().toString().trim());
                    elementName.setText("");
                    Toast.makeText(getContext(), getString(R.string.success_a), Toast.LENGTH_LONG).show();
                } else {
                    elementName.setError(getString(R.string.required));
                }

                symptomsList = dayDao.getAllSymptomsArrayList();
                symptomsAdapter.updateSymptomsAdapter(symptomsList, selectedSymptoms);
            }
        });

        dialog.create();
        dialog.show();

    }

    private void showAddNoteDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_note_dialog);
        dialog.create();

        final EditText noteContentEt = (EditText) dialog.findViewById(R.id.noteContentEt);
        Button addToList = (Button) dialog.findViewById(R.id.addToList);
        Button cancelButton = (Button) dialog.findViewById(R.id.button9);
        Button addToDb = (Button) dialog.findViewById(R.id.addSelectedNotes);

        ListView listView = (ListView) dialog.findViewById(R.id.addedNotesListView);

        notesAdapter = new UniversalSimpleListAdapter(getContext(), UniversalSimpleListAdapter.REQUEST_CODE_NOTES);

        if (selectedNotes.size() != 0 && addedNotesList.size() == 1 && !selectedNotes.contains(getString(R.string.completely_nothing))) {
            addedNotesList.clear();
            addedNotesList = selectedNotes;
        }

        notesAdapter.setSelectedItems(selectedNotes);
        notesAdapter.setListOfNotes(addedNotesList);

        listView.setAdapter(notesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String model = addedNotesList.get(position);
                if (!selectedNotes.contains(model)) {
                    selectedNotes.add(model);
                } else selectedNotes.remove(model);

                notesAdapter.updateNotesAdapter(addedNotesList, selectedNotes);
            }
        });


        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteContentEt.getText().toString().trim().length() != 0) {
                    if(addedNotesList.contains(getString(R.string.completely_nothing))){
                        addedNotesList.clear();
                    }
                    addedNotesList.add(noteContentEt.getText().toString().trim());
                    notesAdapter.updateNotesAdapter(addedNotesList, selectedNotes);
                    noteContentEt.setText("");
                } else {
                    noteContentEt.setError(getString(R.string.required));
                }
            }
        });

        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNotes.contains(getString(R.string.completely_nothing))) {
                    selectedNotes.remove(getString(R.string.completely_nothing));
                }

                if (selectedNotes.size() != 0) {
                    notesTV.setText(convertFromArrayListOfStringToString(selectedNotes));
                } else notesTV.setText(getString(R.string.completely_nothing));
                dialog.cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.create();
        dialog.show();

    }

    private String convertFromArrayListOfStringToString(ArrayList<String> list) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");

        if (list.size() != 0) {
            for (String item : list) {
                buffer.append("-").append(" ").append(item).append("\n");
            }
        } else buffer.append("-").append(" ").append(getString(R.string.completely_nothing));

        return buffer.toString();
    }

    public String convertDateToString(Date date) {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);
        return simpleDate.format(date) + " " + dayOfTheWeek;
    }

    private String convertDateTVToString(Date date){
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return simpleDate.format(date);
    }

}

package com.example.asia.jmpro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.logic.calendar.DateUtilities;

import java.util.Date;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class DiaryAddSingleDayFragment extends Fragment {
    TextView dateToAdd;
    Button changeDatePickerButton;
    Button saveDayToDbButton;
    Button addEatenProductsButton, modEatenProductsButton, removeEatenProductsButton;
    Button addMedicinesButton, modMedicinesButton, removeMedicinesButton;
    Button addSymptomsButton, modSymptomsButton, removeSymptomsButton;
    Button addNoteButton, modNotesButton, removeNoteButton;
    ListView eatenProductsListView, medicinesListView, symptomsListView, notesListView;

    Date dateToAddDate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.diary_add_single_day,container,false);
        dateToAdd = (TextView) diaryFragment.findViewById(R.id.dateToAdd);
        dateToAdd.setText(DateUtilities.currentDay() + "." + (DateUtilities.currentMonth() + 1) + "." + DateUtilities.currentYear());

        changeDatePickerButton = (Button) diaryFragment.findViewById(R.id.changeDateButton);
        saveDayToDbButton = (Button) diaryFragment.findViewById(R.id.saveDayToDbButton);

        addEatenProductsButton = (Button) diaryFragment.findViewById(R.id.addEatenProductsButton);
        modEatenProductsButton = (Button) diaryFragment.findViewById(R.id.modEatenProductsButton);
        removeEatenProductsButton = (Button) diaryFragment.findViewById(R.id.removeEatenProdButton);

        addMedicinesButton = (Button) diaryFragment.findViewById(R.id.addMedicinesButton);
        modMedicinesButton = (Button) diaryFragment.findViewById(R.id.modMedicinesButton);
        removeMedicinesButton = (Button) diaryFragment.findViewById(R.id.removeMedicinesButton);

        addSymptomsButton = (Button) diaryFragment.findViewById(R.id.addSymptomButton);
        modSymptomsButton = (Button) diaryFragment.findViewById(R.id.modSymptomButton);
        removeSymptomsButton = (Button) diaryFragment.findViewById(R.id.removeSymptomButton);

        addNoteButton = (Button) diaryFragment.findViewById(R.id.addNoteButton);
        modNotesButton = (Button) diaryFragment.findViewById(R.id.modNoteButton);
        removeNoteButton = (Button) diaryFragment.findViewById(R.id.removeNoteButton);

        eatenProductsListView = (ListView) diaryFragment.findViewById(R.id.eatenProductsListView);
        medicinesListView = (ListView) diaryFragment.findViewById(R.id.medicinesListView);
        symptomsListView = (ListView) diaryFragment.findViewById(R.id.symptomsListView);
        notesListView = (ListView) diaryFragment.findViewById(R.id.notesListView);


        addEatenProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEatenProductDialog();
            }
        });

        changeDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogOnClick();
            }
        });
        return  diaryFragment;
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int birthYear, int monthOfAYear, int dayOfMonth) {
            dateToAdd.setText(dayOfMonth + "." + (monthOfAYear + 1) + "." + birthYear);
            dateToAdd.setTextColor(getResources().getColor(R.color.colorBlack, null));
            dateToAddDate = DateUtilities.getDate(birthYear, monthOfAYear, dayOfMonth);
        }
    };

    private void showAddEatenProductDialog(){
        Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.universal_simple_content);
                dialog.create();
        EditText elementName = (EditText) dialog.findViewById(R.id.editText);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getString(R.string.select_products));

        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        Button button = (Button) dialog.findViewById(R.id.multiTaskButton);
        Button addToDb = (Button) dialog.findViewById(R.id.button8);

    }

}

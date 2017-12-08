package com.example.asia.jmpro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.adapters.DaysListAdapter;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Note;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.data.db.DayDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmList;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class DiaryMyNotedDaysFragment extends Fragment {
    TextView title;
    ListView allDaysListView;
    DaysListAdapter adapter;
    DayDao dayDao;
    ArrayList<Day> daysList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.fragment_substitutes, container, false);

        dayDao = new DayDao(getContext());
        title = diaryFragment.findViewById(R.id.textView23);
        title.setText(getString(R.string.saved_days));

        allDaysListView = diaryFragment.findViewById(R.id.dedicatedSubstitutesListView);
        daysList = dayDao.getAllSavedDays();

        adapter = new DaysListAdapter(daysList, getContext());
        allDaysListView.setAdapter(adapter);

        allDaysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Day model = daysList.get(position);
                showDialogDayDetails(model);
            }
        });


        return diaryFragment;
    }

    private void showDialogDayDetails(Day model) {
        if(getContext()!=null) {
            final Dialog builder = new Dialog(getContext());
            builder.setContentView(R.layout.day_details_dialog);

            final TextView dayId = builder.findViewById(R.id.dayId);
            dayId.setText(model.getId());

            TextView productsList = builder.findViewById(R.id.eatenProdTV);
            TextView medicinesList = builder.findViewById(R.id.medicTV);
            TextView symptomsList = builder.findViewById(R.id.symptomsTV);
            TextView notesList = builder.findViewById(R.id.noteTV);


            productsList.setText(convertFromArrayListOfStringToString(convertProducts(model.getProductsList())));
            medicinesList.setText(convertFromArrayListOfStringToString(convertMedicines(model.getMedicinesList())));
            symptomsList.setText(convertFromArrayListOfStringToString(convertSymptoms(model.getSymptomsList())));
            notesList.setText(convertFromArrayListOfStringToString(convertNotes(model.getNotesList())));

            Button removeDayFromDb = builder.findViewById(R.id.removeDayButton);
            Button modifyDay = builder.findViewById(R.id.modDayButton);

            modifyDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Day day = dayDao.getDayFromId(dayId.getText().toString());

                    DiaryAddSingleDayFragment diaryAddSingleDayFragment = new DiaryAddSingleDayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", convertDateToString(day.getDate()));
                    bundle.putString("dateDate", day.getDate().toString());
                    diaryAddSingleDayFragment.setArguments(bundle);
                    replaceFragmentContent(diaryAddSingleDayFragment);

                    builder.cancel();

                }
            });

            removeDayFromDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showQuestionDialog(dayId);
                    builder.cancel();
                }
            });

            Button cancelButton = builder.findViewById(R.id.cancelDayButton);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.cancel();
                }
            });

            builder.create();
            builder.show();
        }
    }

    private void showQuestionDialog(final TextView dayId) {
        if(getContext()!=null) {
            final AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.if_u_sure) + " " + dayId.getText().toString() + getString(R.string.from_db))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dayDao.removeSingleDayFromDb(dayId.getText().toString().trim());
                            daysList = dayDao.getAllSavedDays();
                            adapter.updateAdapter(daysList, new ArrayList<String>());
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
            builder.show();
        }

    }

    public  String convertDateToString( Date date) {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);
        return simpleDate.format(date) + " " + dayOfTheWeek;
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

    private ArrayList<String> convertProducts(RealmList<Product> realmList) {
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for (Product item : realmList) {
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertMedicines(RealmList<Medicine> realmList) {
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for (Medicine item : realmList) {
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertSymptoms(RealmList<Symptom> realmList) {
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for (Symptom item : realmList) {
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertNotes(RealmList<Note> realmList) {
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for (Note item : realmList) {
            list.add(item.getNoteContent());
        }
        return list;
    }

    public void replaceFragmentContent(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager!=null) fragmentManager.beginTransaction().replace(R.id.contentDiary, fragment).commit();
    }
}

package com.example.asia.jmpro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.fragment_substitutes,container,false);

        dayDao = new DayDao(getContext());
        title = (TextView) diaryFragment.findViewById(R.id.textView23);
        title.setText(getString(R.string.saved_days));

        allDaysListView = (ListView) diaryFragment.findViewById(R.id.dedicatedSubstitutesListView);
        daysList = dayDao.getAllSavedDays();

        adapter = new DaysListAdapter(daysList,getContext());
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
        final Dialog builder = new Dialog(getContext());
        builder.setContentView(R.layout.day_details_dialog);

        TextView dayId = (TextView) builder.findViewById(R.id.dayId);
        dayId.setText(model.getId());

        TextView productsList = (TextView) builder.findViewById(R.id.eatenProdTV);
        TextView medicinesList = (TextView) builder.findViewById(R.id.medicTV);
        TextView symptomsList = (TextView) builder.findViewById(R.id.symptomsTV);
        TextView notesList = (TextView) builder.findViewById(R.id.noteTV);


        productsList.setText(convertFromArrayListOfStringToString(convertProducts(model.getProductsList())));
        medicinesList.setText(convertFromArrayListOfStringToString(convertMedicines(model.getMedicinesList())));
        symptomsList.setText(convertFromArrayListOfStringToString(convertSymptoms(model.getSymptomsList())));
        notesList.setText(convertFromArrayListOfStringToString(convertNotes(model.getNotesList())));

        Button removeDayFromDb = (Button) builder.findViewById(R.id.removeDayButton);
        Button modifyDay = (Button) builder.findViewById(R.id.modDayButton);
        Button cancelButton = (Button) builder.findViewById(R.id.cancelDayButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        builder.create();
        builder.show();
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

    private ArrayList<String> convertProducts(RealmList<Product> realmList){
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for(Product item :realmList){
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertMedicines(RealmList<Medicine> realmList){
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for(Medicine item :realmList){
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertSymptoms(RealmList<Symptom> realmList){
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for(Symptom item :realmList){
            list.add(item.getName());
        }
        return list;
    }

    private ArrayList<String> convertNotes(RealmList<Note> realmList){
        ArrayList<String> list = new ArrayList<>(realmList.size());

        for(Note item :realmList){
            list.add(item.getNoteContent());
        }
        return list;
    }
}

package com.example.asia.jmpro.data.db;

import android.content.Context;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Note;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.logic.calendar.DateUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by asia on 15/10/2017.
 */

public class DayDao {
    private Realm realmDatabase;
    private Realm privateDatabase;
    private Context context;
    private RealmResults<Product> allProductsList;
    private RealmResults<Medicine> allMedicinesList;
    private RealmResults<Symptom> allSymptomsList;
    private RealmResults<Day> daysList;
    private Day singleDay;
    private int nextId;
    private Day day;
    private Date dateFrom;
    public static final int CODE_PRODUCTS = 1;
    public static final int CODE_MEDICINES = 2;
    public static final int CODE_SYMPTOMS = 3;

    public DayDao(Context context) {
        this.context = context;
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
        this.privateDatabase = DbConnector.getInstance().getPrivateRealmDatabase();
    }

    public ArrayList<Product> getAllProductsArrayList() {
        final ArrayList<Product> list = new ArrayList<>();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allProductsList = realm.where(Product.class).findAll();
            }
        });

        for (Product item : allProductsList) {
            list.add(item);
        }

        return list;
    }

    public void addSingleProductToDb(String productName) {
        final Product product = new Product(productName);
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(product);
            }
        });
    }

    public ArrayList<Medicine> getAllMedicinesArrayList() {
        final ArrayList<Medicine> list = new ArrayList<>();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allMedicinesList = realm.where(Medicine.class).findAll();
            }
        });

        for (Medicine item : allMedicinesList) {
            list.add(item);
        }

        return list;
    }

    public void addSingleMedicineToDb(String medicineName) {
        final Medicine model = new Medicine(medicineName);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(model);
            }
        });
    }

    public void addSingleSymptomToDb(String symptomName) {
        final Symptom model = new Symptom(symptomName);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(model);
            }
        });
    }

    public ArrayList<Symptom> getAllSymptomsArrayList() {
        final ArrayList<Symptom> list = new ArrayList<>();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allSymptomsList = realm.where(Symptom.class).findAll();
            }
        });

        for (Symptom item : allSymptomsList) {
            list.add(item);
        }

        return list;
    }

    public ArrayList<String> getUsersSelectedProductsFromDb(final String date) {
        ArrayList<String> list = new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                singleDay = realm.where(Day.class).equalTo("id", date).findFirst();
            }
        });

        if (singleDay != null) {
            RealmList<Product> products = singleDay.getProductsList();

            for (Product item : products) {
                list.add(item.getName());
            }
        } else list.add(context.getString(R.string.completely_nothing));

        return list;
    }

    public ArrayList<String> getUsersSelectedMedicinesFromDb(final String date) {
        ArrayList<String> list = new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                singleDay = realm.where(Day.class).equalTo("id", date).findFirst();
            }
        });

        if (singleDay != null) {
            RealmList<Medicine> medicines = singleDay.getMedicinesList();

            for (Medicine item : medicines) {
                list.add(item.getName());
            }
        } else list.add(context.getString(R.string.completely_nothing));

        return list;
    }

    public ArrayList<String> getUsersSelectedSymptomsFromDb(final String date) {
        ArrayList<String> list = new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                singleDay = realm.where(Day.class).equalTo("id", date).findFirst();
            }
        });

        if (singleDay != null) {
            RealmList<Symptom> symptoms = singleDay.getSymptomsList();

            for (Symptom item : symptoms) {
                list.add(item.getName());
            }
        } else list.add(context.getString(R.string.completely_nothing));

        return list;
    }

    public ArrayList<String> getUsersSelectedNotesFromDb(final String date) {
        ArrayList<String> list = new ArrayList<>();
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                singleDay = realm.where(Day.class).equalTo("id", date).findFirst();
            }
        });

        if (singleDay != null) {
            RealmList<Note> notes = singleDay.getNotesList();
            for (Note item : notes) {
                list.add(item.getNoteContent());
            }
        } else list.add(context.getString(R.string.completely_nothing));

        return list;
    }


    public void insertSingleDayToDatabase(Date dateToAddDate,
                                          ArrayList<String> selectedProducts,
                                          ArrayList<String> selectedMedicines,
                                          ArrayList<String> selectedSymptoms,
                                          ArrayList<String> selectedNotes) {

        String alternativeName = context.getString(R.string.completely_nothing);
        RealmList<Product> products = new RealmList<>();
        RealmList<Medicine> medicines = new RealmList<>();
        RealmList<Symptom> symptoms = new RealmList<>();
        RealmList<Note> notes = new RealmList<>();

        final Day day = new Day();
        day.setDate(dateToAddDate);

        if (selectedProducts.size() != 0) {
            for (String item : selectedProducts) {
                products.add(new Product(item));
            }
        } else products.add(new Product(alternativeName));
        day.setProductsList(products);

        if (selectedMedicines.size() != 0) {
            for (String item : selectedMedicines) {
                medicines.add(new Medicine(item));
            }
        } else medicines.add(new Medicine(alternativeName));
        day.setMedicinesList(medicines);

        if (selectedSymptoms.size() != 0) {
            for (String item : selectedSymptoms) {
                symptoms.add(new Symptom(item));
            }
        } else symptoms.add(new Symptom(alternativeName));
        day.setSymptomsList(symptoms);

        if (selectedNotes.size() != 0) {
            for (String item : selectedNotes) {
                Note note = new Note(item);
                note.setId(getNextNoteId());
                notes.add(note);
            }
        } else notes.add(new Note(alternativeName));
        day.setNotesList(notes);

        day.setId(dateToAddDate);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(day);
            }
        });
    }

    private int getNextNoteId() {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number num = realm.where(Note.class).max("id");
                if (num != null) {
                    nextId = (int) (long) (realm.where(Note.class).max("id")) + 1;
                } else {
                    nextId = 0;
                }
            }
        });

        return nextId;
    }

    public ArrayList<Day> getAllSavedDays() {
        ArrayList<Day> list = new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                daysList = realm.where(Day.class).findAll();
            }
        });

        for (Day item : daysList) {
            list.add(item);
        }

        return list;
    }

    public Day getDayFromId(final String id) {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                day = realm.where(Day.class).equalTo("id", id).findFirst();
            }
        });
        return day;
    }

    public void removeSingleDayFromDb(final String dayId) {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                day = realm.where(Day.class).equalTo("id", dayId).findFirst();
                day.deleteFromRealm();
            }
        });
    }

    public ArrayList<Day> getLastSevenDays(final Date dateTo) {
        dateFrom = DateUtilities.getDate(DateUtilities.currentYear(),DateUtilities.currentMonth(),DateUtilities.currentDay()-8);
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                daysList = realm.where(Day.class)
                        .between("date", dateFrom, dateTo)
                        .findAll();
            }
        });

        ArrayList<Day> newList = new ArrayList<>(daysList.size());
        for(Day item : daysList){
            newList.add(item);
        }
        return newList;
    }

    public ArrayList<Day> getCurrentMonthDays(final Date currentDate) {
        dateFrom = DateUtilities.getDate(DateUtilities.currentYear(),DateUtilities.currentMonth()-1,DateUtilities.currentDay());

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                daysList = realm.where(Day.class)
                        .between("date", dateFrom, currentDate)
                        .findAll();
            }
        });

        ArrayList<Day> daysItemList = new ArrayList<>();
        int currentMonth = DateUtilities.currentMonth();
        for (Day item : daysList) {
            if(currentMonth == getMonthOfYear(item.getDate())){
                daysItemList.add(item);
            }
        }

        return daysItemList;
    }

    public ArrayList<Day> getCurrentYearDays(final Date currentDate) {
        ArrayList<Day> daysItemList = new ArrayList<>();
        dateFrom = DateUtilities.getDate(DateUtilities.currentYear()-1,DateUtilities.currentMonth(),DateUtilities.currentDay());

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                daysList = realm.where(Day.class)
                        .between("date", dateFrom, currentDate)
                        .findAll();
            }
        });

        for (Day item : daysList) {
                daysItemList.add(item);
        }

        return daysItemList;
    }

    private int getMonthOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public ArrayList<String> getAllEatenProductsString(ArrayList<Day> dayArrayList){
        ArrayList<String> productsArrayList = new ArrayList<>();
        RealmList<Product> eatenProductsList;

        for(Day item : dayArrayList) {
            eatenProductsList = item.getProductsList();

            for(Product product : eatenProductsList) {
                if (!productsArrayList.contains(product.getName())) {
                    productsArrayList.add(product.getName());
                }
            }
        }

        if(productsArrayList.size()>1 && productsArrayList.contains(context.getString(R.string.completely_nothing))){
            productsArrayList.remove(context.getString(R.string.completely_nothing));
        }

        return productsArrayList;
    }

    public ArrayList<String> getAllMedicinesString(ArrayList<Day> dayArrayList){
        ArrayList<String> medicinesArrayList = new ArrayList<>();
        RealmList<Medicine> medicinesList;

        for(Day item : dayArrayList) {
            medicinesList = item.getMedicinesList();

            for(Medicine medicine : medicinesList) {
                if (!medicinesArrayList.contains(medicine.getName())) {
                    medicinesArrayList.add(medicine.getName());
                }
            }
        }

        if(medicinesArrayList.size()>1 && medicinesArrayList.contains(context.getString(R.string.completely_nothing))){
            medicinesArrayList.remove(context.getString(R.string.completely_nothing));
        }

        return medicinesArrayList;
    }

    public ArrayList<String> getAllSymptomsString(ArrayList<Day> dayArrayList){
        ArrayList<String> symptomsArrayList = new ArrayList<>();
        RealmList<Symptom> symptomsList;

        for(Day item : dayArrayList) {
            symptomsList = item.getSymptomsList();

            for(Symptom symptom : symptomsList) {
                if (!symptomsArrayList.contains(symptom.getName())) {
                    symptomsArrayList.add(symptom.getName());
                }
            }
        }

        if(symptomsArrayList.size()>1 && symptomsArrayList.contains(context.getString(R.string.completely_nothing))){
            symptomsArrayList.remove(context.getString(R.string.completely_nothing));
        }

        return symptomsArrayList;
    }

    public String[] getAllEatenProductsList(ArrayList<Day> dayArrayList){
        ArrayList<String> productsArrayList = getAllEatenProductsString(dayArrayList);

        String[] newList = new String[productsArrayList.size()+2];
        for(int i = 0 ; i < productsArrayList.size(); i++){
            newList[i] = productsArrayList.get(i);
        }

        return newList;
    }

    public String[] getAllEatenMedicinesList(ArrayList<Day> dayArrayList){
        ArrayList<String> medicinesArrayList = getAllMedicinesString(dayArrayList);

        String[] newList = new String[medicinesArrayList.size()];
        for(int i = 0; i < medicinesArrayList.size(); i++){
            newList[i] = medicinesArrayList.get(i);
        }

        return newList;
    }

    public String[] getAllSymptomsList(ArrayList<Day> dayArrayList){
        ArrayList<String> symptomsArrayList = getAllSymptomsString(dayArrayList);

        String[] newList = new String[symptomsArrayList.size()+2];

        for(int i = 0; i < symptomsArrayList.size(); i++){
            newList[i] = symptomsArrayList.get(i);
        }

        return newList;
    }

    public ArrayList<Float> countEverySingleElements(ArrayList<String> arrayList, ArrayList<Day> dayArrayList, int code){
        ArrayList<String> elements;

        if(arrayList.size() > 1 && arrayList.contains(context.getString(R.string.completely_nothing))){
            arrayList.remove(context.getString(R.string.completely_nothing));
        }

        ArrayList<Integer> countedList = new ArrayList<>(arrayList.size());
        switch (code){
            case CODE_PRODUCTS:
                for(String item : arrayList) {
                    int count = 0;
                    for(Day day : dayArrayList){
                        elements = convertRealmListToStringListProducts(day.getProductsList());
                        if(elements.contains(item)){
                            count += 1;
                        }
                    }
                    countedList.add(count);
                }
                break;
            case CODE_MEDICINES:
                for(String item : arrayList) {
                    int count = 0;
                    for(Day day : dayArrayList){
                        elements = convertRealmListToStringListMedicines(day.getMedicinesList());
                        if(elements.contains(item)){
                            count += 1;
                        }
                    }
                    countedList.add(count);
                }
                break;
            case CODE_SYMPTOMS:
                for(String item : arrayList) {
                    int count = 0;
                    for(Day day : dayArrayList){
                        elements = convertRealmListToStringListSymptoms(day.getSymptomsList());
                        if(elements.contains(item)){
                            count += 1;
                        }
                    }
                    countedList.add(count);
                }
                break;
        }

        int allItems = 0;
        for(int item : countedList){
            allItems += item;
        }

        ArrayList<Float> percentageList = new ArrayList<>(countedList.size());

        float value;
        for(int item : countedList){
            if(allItems!= 0) {
                value = item * 100 / allItems;
            } else value = 0;

            percentageList.add(value);
        }


        return percentageList;
    }

    private ArrayList<String> convertRealmListToStringListProducts(RealmList<Product> list){
        ArrayList<String > newList = new ArrayList<>(list.size());

        for(Product item : list){
            newList.add(item.getName());
        }

        return newList;
    }

    private ArrayList<String> convertRealmListToStringListSymptoms(RealmList<Symptom> list){
        ArrayList<String > newList = new ArrayList<>(list.size());

        for(Symptom item : list){
            newList.add(item.getName());
        }

        return newList;
    }

    private ArrayList<String> convertRealmListToStringListMedicines(RealmList<Medicine> list){
        ArrayList<String > newList = new ArrayList<>(list.size());

        for(Medicine item : list){
            newList.add(item.getName());
        }

        return newList;
    }

}

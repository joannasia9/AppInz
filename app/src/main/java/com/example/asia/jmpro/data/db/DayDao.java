package com.example.asia.jmpro.data.db;

import android.content.Context;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Note;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;

import java.util.ArrayList;
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
    private RealmResults<Day> allDaysList;
    private Day singleDay;
    private int nextId;
    private Day day;
    private Note note;

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


    public ArrayList<Day> getAllSavedDays(){
        ArrayList<Day> list= new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allDaysList = realm.where(Day.class).findAll();
            }
        });

        for(Day item : allDaysList){
            list.add(item);
        }

        return list;
    }


    public Day getDayFromId(final String id){
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                day = realm.where(Day.class).equalTo("id",id).findFirst();
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
}

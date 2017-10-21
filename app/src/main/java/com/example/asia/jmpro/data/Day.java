package com.example.asia.jmpro.data;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asia on 15/10/2017.
 */

public class Day extends RealmObject {
    @PrimaryKey
    private String id;

    private Date date;
    private RealmList<Product> productsList;
    private RealmList<Medicine> medicinesList;
    private RealmList<Symptom> symptomsList;
    private RealmList<Note> notesList;

    public Day() {
    }

    public String getId() {
        return id;
    }

    public void setId(Date date) {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
        this.id = simpleDate.format(date) + " " + dayOfTheWeek;
    }

    public Day (Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(RealmList<Product> productsList) {
        this.productsList = productsList;
    }

    public RealmList<Medicine> getMedicinesList() {
        return medicinesList;
    }

    public void setMedicinesList(RealmList<Medicine> medicinesList) {
        this.medicinesList = medicinesList;
    }

    public RealmList<Symptom> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(RealmList<Symptom> symptomsList) {
        this.symptomsList = symptomsList;
    }

    public RealmList<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(RealmList<Note> notesList) {
        this.notesList = notesList;
    }
}

package com.example.asia.jmpro.data.modules;

import com.example.asia.jmpro.data.AllergenString;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Note;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.models.Allergen;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {Allergen.class, Place.class, AllergenString.class, Note.class, Day.class, Product.class, Medicine.class, Symptom.class})
public class PrivateEntitiesModule {
}

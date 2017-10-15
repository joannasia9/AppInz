package com.example.asia.jmpro.data.modules;

import com.example.asia.jmpro.data.AllergenString;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.models.Allergen;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {Allergen.class, Place.class, AllergenString.class})
public class PrivateEntitiesModule {
}

package com.example.asia.jmpro.data.modules;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.SuggestedPlace;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.data.UserRealm;
import com.example.asia.jmpro.models.Allergen;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {UserRealm.class, AllergenRealm.class, Allergen.class, SubstituteRealm.class, SuggestedPlace.class, Product.class, Medicine.class, Symptom.class})
public class GlobalEntitiesModule {
}

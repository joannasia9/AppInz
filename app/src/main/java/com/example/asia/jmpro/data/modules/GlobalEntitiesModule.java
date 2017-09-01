package com.example.asia.jmpro.data.modules;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.UserRealm;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {UserRealm.class, AllergenRealm.class, SubstituteRealm.class})
public class GlobalEntitiesModule {
}

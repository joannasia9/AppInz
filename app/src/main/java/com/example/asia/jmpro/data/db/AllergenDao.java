package com.example.asia.jmpro.data.db;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.SubstituteRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by asia on 29/08/2017.
 */

public class AllergenDao {
    private Realm realmDatabase;
    private RealmResults<AllergenRealm> allergensList=null;


    public AllergenDao() {
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
    }

    public void insertAllergenItem(final String allergenName) {
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AllergenRealm allergenItem = realm.createObject(AllergenRealm.class);
                allergenItem.setAllergenName(allergenName);
                allergenItem.setSubstitutes(null);
            }
        });
    }

    public ArrayList<String> getAllAllergensNames() {
        ArrayList<String> list = new ArrayList<>();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergensList = realm.where(AllergenRealm.class).findAll();
            }
        });

        for (AllergenRealm item : allergensList) {
            list.add(item.getAllergenName());
        }
        return list;
    }

    public void insertSingleSubstitute(String substituteName) {
        realmDatabase.beginTransaction();
        SubstituteRealm substituteItem = realmDatabase.createObject(SubstituteRealm.class);
        substituteItem.setName(substituteName);
        realmDatabase.commitTransaction();
    }

    public void insertSubstituteListToAllergenObject(String allergenName, ArrayList<String> substitutesList) {
        realmDatabase.beginTransaction();
        AllergenRealm allergen = realmDatabase.where(AllergenRealm.class)
                .equalTo("allergenName", allergenName)
                .findFirst();

        for (String substituteItem : substitutesList) {
            SubstituteRealm substitute = realmDatabase.where(SubstituteRealm.class)
                    .equalTo("name", substituteItem)
                    .findFirst();

            allergen.substitutes.add(substitute);
        }

        realmDatabase.commitTransaction();
    }

}


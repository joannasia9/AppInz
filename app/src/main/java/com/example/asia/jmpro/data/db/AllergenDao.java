package com.example.asia.jmpro.data.db;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by asia on 29/08/2017.
 */

public class AllergenDao {
    private Realm realmDatabase;
    private Realm privateDatabase;
    private RealmResults<AllergenRealm> allergensList=null;
    private RealmResults<Allergen> myAllergensList = null;


    public AllergenDao() {
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
        this.privateDatabase = DbConnector.getInstance().getPrivateRealmDatabase();
    }

    public void insertAllergenItem(final String allergenName) {
        //IF DOES NOT EXIST
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

    public ArrayList<Allergen> getAllAllergens() {
        ArrayList<Allergen> list = new ArrayList<>();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergensList = realm.where(AllergenRealm.class).findAll();
            }
        });

        for (AllergenRealm item : allergensList) {
            Allergen aItem = new Allergen(item.getAllergenName(),false);
            list.add(aItem);
        }
        return list;
    }

    public void insertSingleSubstitute(String substituteName) {
       // --> IF DOES NOT EXIST
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

    public void insertMyAllergenList(final List<Allergen> list) {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Allergen item : list){
                    //IF DOES NOT EXIST --> issue
                    Allergen myAllergenItem = realm.createObject(Allergen.class);
                    myAllergenItem.setName(item.getName());
                    myAllergenItem.setSelected(item.isSelected());
                }
            }
        });
    }

    public List<Allergen> getMyAllergensList() {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                myAllergensList = realm.where(Allergen.class).findAll();
            }
        });
        return myAllergensList;
    }
}


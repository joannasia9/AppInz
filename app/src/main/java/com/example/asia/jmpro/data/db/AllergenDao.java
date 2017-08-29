package com.example.asia.jmpro.data.db;

import android.widget.EditText;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.DbConnectorSingleton;

import io.realm.Realm;

/**
 * Created by asia on 29/08/2017.
 *
 */

public class AllergenDao {
    private DbConnectorSingleton dbConnectorSingleton;
    private Realm realmDatabase;

    public AllergenDao(String currentUserLogin, String currentUserPassword){
        this.dbConnectorSingleton = DbConnectorSingleton.getInstance(currentUserLogin,currentUserPassword);
    }


    public Realm getRealmDatabase() {
        dbConnectorSingleton.getRealmDatabase(new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                realmDatabase = realm;
            }
        });
        return realmDatabase;
    }

    public void insertAllergen(EditText allergenName){
        if(realmDatabase!=null) {
            realmDatabase.beginTransaction();
            AllergenRealm allergenRealm = realmDatabase.createObject(AllergenRealm.class);
            allergenRealm.setAllergenName(allergenName.getText().toString().trim());
            realmDatabase.commitTransaction();
        }
    }
}


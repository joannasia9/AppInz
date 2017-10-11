package com.example.asia.jmpro.data.db;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.AllergenString;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by asia on 29/08/2017.
 */

public class AllergenDao {
    private Realm realmDatabase;
    private Realm privateDatabase;
    private RealmResults<AllergenRealm> allergensList = null;
    private RealmResults<Allergen> myAllergensList = null;
    private AllergenRealm allergenRealm, allergenRealmToDeleteFromPrivateDb,allergenRealmToDeleteFromGlobalDb;
    private AllergenRealm updatedAllergenRealm;
    private RealmResults<AllergenString> allergens;
    private AllergenString allergenString;


    public AllergenDao() {
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
        this.privateDatabase = DbConnector.getInstance().getPrivateRealmDatabase();
    }

    public void insertAllergenItem(final String allergenName) {
        final AllergenRealm allergenRealm = new AllergenRealm();
        allergenRealm.setAllergenName(allergenName);
        allergenRealm.setSubstitutes(null);

        final Allergen allergen = new Allergen();
        allergen.setName(allergenName);
        allergen.setSelected(true);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(allergen);
            }
        });
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
            Allergen aItem = new Allergen(item.getAllergenName(), false);
            list.add(aItem);
        }
        return list;
    }

    public ArrayList<AllergenRealm> getAllAllergenRealm() {
        ArrayList<AllergenRealm> list = new ArrayList<>();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergensList = realm.where(AllergenRealm.class).findAll();
            }
        });

        for (AllergenRealm item : allergensList) {
            list.add(item);
        }
        return list;
    }

    public void insertMyAllergenList(final List<Allergen> list) {
        Realm.Transaction transactionClearList = new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Allergen.class);
            }
        };
        privateDatabase.executeTransaction(transactionClearList);

        Realm.Transaction transactionAdd = new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Allergen item : list) {
                    Allergen myAllergen = realm.createObject(Allergen.class);
                    myAllergen.setName(item.getName());
                    myAllergen.setSelected(item.isSelected());
                }


            }
        };
        privateDatabase.executeTransaction(transactionAdd);


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

    public boolean isAllergenExist(final String allergenName){

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", allergenName).findFirst();
            }
        });

        return allergenRealm != null;
    }

    public void insertAllergenItemToTheGlobalDB(String allergenName) {
        final AllergenRealm allergenRealm = new AllergenRealm();
        allergenRealm.setAllergenName(allergenName);
        allergenRealm.setSubstitutes(null);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });
    }

    public void insertAllergenItemToThePrivateDB(String allergenName) {
        final AllergenString allergenString = new AllergenString();
        allergenString.setName(allergenName);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenString);
            }
        });
    }

    public void deleteAllergenFromGlobalDb(final String model) {
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AllergenRealm allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", model).findFirst();
                allergenRealm.deleteFromRealm();
            }
        });
    }

    public void deleteAllergenFromPrivateDb(final String name) {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              AllergenString  allergenString = realm.where(AllergenString.class).equalTo("name", name).findFirst();
                allergenString.deleteFromRealm();
            }
        });
    }

    public void addSingleAllergenStringItemToPrivateDb(String name){
        final AllergenString allergenString = new AllergenString();
        allergenString.setName(name);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenString);
            }
        });
    }

    public ArrayList<String> getAllAllergensRealmAddedByMe() {
        ArrayList<String> list = new ArrayList<>();
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergens = realm.where(AllergenString.class).findAll();
            }
        });

        for(AllergenString item : allergens){
            list.add(item.getName());
        }

        return list;
    }

    public ArrayList<AllergenString> getAllAllergensStringAddedByMe() {
        ArrayList<AllergenString> list = new ArrayList<>();
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergens = realm.where(AllergenString.class).findAll();
            }
        });

        for(AllergenString item : allergens){
            list.add(item);
        }

        return list;
    }

    public void updateAllergenRealm(final String oldAllergenName, final String newAllergenName, final ArrayList<SubstituteRealm> substituteRealms) {
        final RealmList<SubstituteRealm> list = new RealmList<>();

        for(SubstituteRealm item : substituteRealms){
            list.add(item);
        }

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", oldAllergenName).findFirst();
                allergenRealm.deleteFromRealm();
            }
        });

        updatedAllergenRealm = new AllergenRealm();
        updatedAllergenRealm.setAllergenName(newAllergenName);
        updatedAllergenRealm.setSubstitutes(list);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(updatedAllergenRealm);
            }
        });

    }
}


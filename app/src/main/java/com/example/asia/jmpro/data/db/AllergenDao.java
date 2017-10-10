package com.example.asia.jmpro.data.db;

import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.DbConnector;
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
    private RealmResults<AllergenRealm> allergensList = null;
    private RealmResults<Allergen> myAllergensList = null;
    private AllergenRealm allergenRealm;
    private ArrayList<Allergen> allergenList;
    private ArrayList<AllergenRealm> allergenRealmList;


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

        final Allergen allergen = new Allergen();
        allergen.setName(allergenName);
        allergen.setSelected(true);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });
    }

    public void insertAllergenItemToThePrivateDB(String allergenName) {
        final AllergenRealm allergenRealm = new AllergenRealm();
        allergenRealm.setAllergenName(allergenName);
        allergenRealm.setSubstitutes(null);

        final Allergen allergen = new Allergen();
        allergen.setName(allergenName);
        allergen.setSelected(true);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });
    }

    public void deleteAllergenFromGlobalDb(final Allergen model) {
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", model.getName()).findFirst();
                allergenRealm.deleteFromRealm();
            }
        });
    }

    public void deleteAllergenFromPrivateDb(final Allergen model) {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", model.getName()).findFirst();
                allergenRealm.deleteFromRealm();
            }
        });
    }

    public void addAllergensRealmToPrivateDb(ArrayList<Allergen> list){
        for(Allergen item : list){
        AllergenRealm allergenRealm = new AllergenRealm();
        allergenRealm.setAllergenName(item.getName());
        allergenRealm.setSubstitutes(null);
            allergenRealmList.add(allergenRealm);
        }

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(AllergenRealm item : allergenRealmList){
                    realm.copyToRealmOrUpdate(item);
                }
            }
        });
    }

    public void addSingleAllergenRealmItemToPrivateDb(String name){
        final AllergenRealm allergenRealm = new AllergenRealm();
        allergenRealm.setAllergenName(name);
        allergenRealm.setSubstitutes(null);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });
    }

    public ArrayList<Allergen> getAllAllergensRealmAddedByMe() {
        allergenList = new ArrayList<>();
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergensList = realm.where(AllergenRealm.class).findAll();
            }
        });

        for(AllergenRealm item : allergensList){
            Allergen a = new Allergen(item.getAllergenName(),false);
            allergenList.add(a);
        }

        return  allergenList;
    }

    public ArrayList<String> getAllAllergensRealmAddedByMeString() {
        ArrayList<String> allergenList = new ArrayList<>();

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergensList = realm.where(AllergenRealm.class).findAll();
            }
        });

        for(AllergenRealm item : allergensList){
            allergenList.add(item.getAllergenName());
        }

        return  allergenList;
    }
}


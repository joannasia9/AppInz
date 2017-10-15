package com.example.asia.jmpro.data.db;

import android.content.Context;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.SubstituteRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class SubstituteDao {
    private Context context;
    private Realm realmDatabase;
    private AllergenRealm allergenRealm;
    private ArrayList<String> stringSubstitutesList;
    private RealmList<SubstituteRealm> allergensSubstitutesList;
    private ArrayList<SubstituteRealm> list;
    private List<SubstituteRealm> allSubstitutesList;


    public SubstituteDao(Context c) {
        realmDatabase = DbConnector.getInstance().getRealmDatabase();
        this.context = c;
    }

    public void addSubstituteToDatabase(String substitute, final String allergen){
        final SubstituteRealm substituteRealm = new SubstituteRealm(substitute);

        allergenRealm = new AllergenRealm();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(substituteRealm);

                allergenRealm = new AllergenRealm();
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", allergen).findFirst();
                allergensSubstitutesList = allergenRealm.getSubstitutes();
                allergensSubstitutesList.sort("name");

                stringSubstitutesList = fromSubstituteRealmToStringList(allergensSubstitutesList);


                if(allergensSubstitutesList.size()!= 0) {
                    if(!stringSubstitutesList.contains(substituteRealm.getName())){
                        allergensSubstitutesList.add(substituteRealm);
                    }
                } else {
                    allergensSubstitutesList.add(substituteRealm);
                }
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });

    }

    private ArrayList<String> fromSubstituteRealmToStringList(RealmList<SubstituteRealm> list) {
        ArrayList<String> newList = new ArrayList<>(list.size());
        for(SubstituteRealm item : list){
            newList.add(item.getName());
        }
        return newList;
    }

    public ArrayList<SubstituteRealm> getAllAllergensSubstituteList(final String allergenName) {
        ArrayList<SubstituteRealm>  allAllergensSubstitutesList = new ArrayList<>();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = new AllergenRealm();
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", allergenName).findFirst();
                allergensSubstitutesList = allergenRealm.getSubstitutes();
            }
        });

        if(allergensSubstitutesList.size()!=0) {
            for (SubstituteRealm item : allergensSubstitutesList) {
                allAllergensSubstitutesList.add(item);
            }
        } else {
            allAllergensSubstitutesList.add(new SubstituteRealm(context.getString(R.string.no_result)));
        }

        return allAllergensSubstitutesList;
    }

    public void removeSubstituteFromDatabase(final String allergenName, final String substituteName, final int position) {
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allergenRealm = new AllergenRealm();
                allergenRealm = realm.where(AllergenRealm.class).equalTo("allergenName", allergenName).findFirst();
                allergensSubstitutesList = allergenRealm.getSubstitutes();

                stringSubstitutesList = fromSubstituteRealmToStringList(allergensSubstitutesList);


                if(allergensSubstitutesList.size()!= 0) {
                    if(stringSubstitutesList.contains(substituteName)){
                        allergensSubstitutesList.remove(position);
                    }
                }
                realm.copyToRealmOrUpdate(allergenRealm);
            }
        });
    }


    public ArrayList<SubstituteRealm> getAllSubstitutesList() {
        list = new ArrayList<>();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allSubstitutesList = realm.where(SubstituteRealm.class).findAll();
            }
        });

        for(SubstituteRealm item : allSubstitutesList){
            list.add(item);
        }

        return list;
    }
}

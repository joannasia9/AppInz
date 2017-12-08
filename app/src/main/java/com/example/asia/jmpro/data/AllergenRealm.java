package com.example.asia.jmpro.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by asia on 24/08/2017.
 *
 */
@RealmClass
public class AllergenRealm extends RealmObject {
    @PrimaryKey
    private String allergenName;
    private RealmList<SubstituteRealm> substitutes;


    public AllergenRealm(){

    }

    public String getAllergenName() {
        return allergenName;
    }

    public void setAllergenName(String allergenName) {
        this.allergenName = allergenName;
    }

    public RealmList<SubstituteRealm> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(RealmList<SubstituteRealm> substitutes) {
        this.substitutes = substitutes;
    }

}

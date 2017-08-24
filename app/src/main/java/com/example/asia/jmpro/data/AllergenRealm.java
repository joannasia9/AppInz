package com.example.asia.jmpro.data;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by asia on 24/08/2017.
 *
 */

public class AllergenRealm extends RealmObject {
    String allergenName;
    RealmList<SubstituteRealm> substitues;


    public String getAllergenName() {
        return allergenName;
    }

    public void setAllergenName(String allergenName) {
        this.allergenName = allergenName;
    }

    public RealmList<SubstituteRealm> getSubstitues() {
        return substitues;
    }

    public void setSubstitues(RealmList<SubstituteRealm> substitues) {
        this.substitues = substitues;
    }

}

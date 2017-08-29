package com.example.asia.jmpro.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by asia on 24/08/2017.
 *
 */

public class AllergenRealm extends RealmObject {
    @Required
    private String allergenName;
    private RealmList<SubstituteRealm> substitutes;


    public String getAllergenName() {
        return allergenName;
    }

    public void setAllergenName(String allergenName) {
        this.allergenName = allergenName;
    }

    public RealmList<SubstituteRealm> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(RealmList<SubstituteRealm> substitues) {
        this.substitutes = substitues;
    }

}

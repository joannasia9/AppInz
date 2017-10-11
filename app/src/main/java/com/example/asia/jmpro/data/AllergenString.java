package com.example.asia.jmpro.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asia on 11/10/2017.
 *
 */

public class AllergenString extends RealmObject {
    @PrimaryKey
    private String name;

    public AllergenString() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

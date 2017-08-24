package com.example.asia.jmpro.data;

import io.realm.RealmObject;

/**
 * Created by asia on 24/08/2017.
 */

public class SubstituteRealm extends RealmObject {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

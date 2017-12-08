package com.example.asia.jmpro.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class Symptom extends RealmObject {
    @PrimaryKey
    private String name;

    public Symptom() {
    }

    public Symptom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.asia.jmpro.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by asia on 24/08/2017.
 *
 */
@RealmClass
public class SubstituteRealm extends RealmObject {

    public SubstituteRealm(String name){
        setName(name);
    }
    public SubstituteRealm(){
    }

    @PrimaryKey
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

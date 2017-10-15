package com.example.asia.jmpro.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asia on 15/10/2017.
 */

public class Product extends RealmObject {
    @PrimaryKey
    private String name;


    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

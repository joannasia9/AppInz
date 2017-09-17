package com.example.asia.jmpro.data;

import com.example.asia.jmpro.models.Allergen;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by asia on 16/09/2017.
 */

public class SuggestedPlace extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String name;

    private double longitude;
    private double latitude;
    private RealmList<Allergen> usersAllergensList;

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = this.latitude + " " + this.longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public RealmList<Allergen> getUsersAllergensList() {
        return usersAllergensList;
    }

    public void setUsersAllergensList(RealmList<Allergen> userAllergensList) {
        this.usersAllergensList = userAllergensList;
    }
}

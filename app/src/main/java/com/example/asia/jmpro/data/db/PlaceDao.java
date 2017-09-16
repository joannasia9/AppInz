package com.example.asia.jmpro.data.db;

import android.location.Location;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.data.SuggestedPlace;
import com.example.asia.jmpro.models.Allergen;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by asia on 16/09/2017.
 */

public class PlaceDao {
    private Realm realmDatabase;
    private Realm privateDatabase;
    private RealmResults<Allergen> usersAllergenList;
    private RealmList<Allergen> list = null;

    public PlaceDao() {
        DbConnector dbConnector = DbConnector.getInstance();
        this.realmDatabase = dbConnector.getRealmDatabase();
        this.privateDatabase = dbConnector.getPrivateRealmDatabase();
    }

    public void addFavouritePlaceToDatabase(final String placeName, Location location) {
        final Place place = new Place();
        place.setLatitude(location.getLatitude());
        place.setLongitude(location.getLongitude());
        place.setName(placeName);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(realm.where(Place.class).equalTo("name", placeName).findFirst() == null) {
                    int nextID = (int) (realm.where(Place.class).count() + 1);
                    place.setId(nextID);
                    realm.copyToRealmOrUpdate(place);
                }
            }
        });
    }

    public void addToSuggestedPlacesDatabase(String placeName, Location location){
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                usersAllergenList = realm.where(Allergen.class).findAll();
            }
        });

        for(Allergen item : usersAllergenList){
            list.add(item);
        }

        SuggestedPlace suggestedPlace = new SuggestedPlace();
        suggestedPlace.setLatitude(location.getLatitude());
        suggestedPlace.setLongitude(location.getLongitude());
        suggestedPlace.setId();
        suggestedPlace.setName(placeName);
        suggestedPlace.setUserAllergensList(list);

    }
}

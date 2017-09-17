package com.example.asia.jmpro.data.db;

import android.content.Context;
import android.location.Location;

import com.example.asia.jmpro.R;
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
    private Context context;
    private Realm realmDatabase;
    private Realm privateDatabase;
    private int nextID;
    private RealmResults<Allergen> usersAllergenList;
    private RealmList<Allergen> allAllergensOfSinglePlace = new RealmList<>();

    public PlaceDao(Context c) {
        DbConnector dbConnector = DbConnector.getInstance();
        this.realmDatabase = dbConnector.getRealmDatabase();
        this.privateDatabase = dbConnector.getPrivateRealmDatabase();
        this.context = c;
    }

    public void addFavouritePlaceToDatabase(final String placeName, Location location) {
        final Place place = new Place();
        place.setLatitude(location.getLatitude());
        place.setLongitude(location.getLongitude());
        place.setName(placeName);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                    nextID = (int) (realm.where(Place.class).count() + 1);
                    place.setId(nextID);
                    realm.copyToRealmOrUpdate(place);
            }
        });
    }

    public void addSuggestedPlaceToDatabase(String placeName, Location location) {
        final SuggestedPlace suggestedPlace = new SuggestedPlace();
        suggestedPlace.setLatitude(location.getLatitude());
        suggestedPlace.setLongitude(location.getLongitude());
        suggestedPlace.setId();
        suggestedPlace.setName(placeName);

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                usersAllergenList = realm.where(Allergen.class).findAll();
            }
        });

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SuggestedPlace place = realm.where(SuggestedPlace.class).equalTo("id",suggestedPlace.getId()).findFirst();

                if(place != null) {
                    allAllergensOfSinglePlace = place.getUsersAllergensList();
                }
            }
        });

        Allergen allergen = new Allergen(context.getString(R.string.nothing), false);

        if(usersAllergenList.size()!= 0) {
            for (Allergen item : usersAllergenList) {
                if(!allAllergensOfSinglePlace.contains(item)) {
                    allAllergensOfSinglePlace.add(item);
                }
            }
        } else if(!allAllergensOfSinglePlace.contains(allergen)) allAllergensOfSinglePlace.add(allergen);

        suggestedPlace.setUsersAllergensList(allAllergensOfSinglePlace);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(suggestedPlace);
            }
        });

    }
}

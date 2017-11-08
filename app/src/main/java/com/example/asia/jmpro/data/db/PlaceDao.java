package com.example.asia.jmpro.data.db;

import android.content.Context;
import android.location.Location;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.data.SuggestedPlace;
import com.example.asia.jmpro.models.Allergen;

import java.util.ArrayList;
import java.util.List;

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
    private Integer nextID;
    private List<Allergen> usersAllergenList;
    private RealmResults<Place> usersFavouritePlacesList;
    private RealmResults<SuggestedPlace> suggestedPlacesList;
    private RealmResults<Place> favouritePlacesList;
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
                if (realm.where(Place.class).equalTo("name", place.getName()).findFirst() == null) {
                    Number num = realm.where(Place.class).max("id");
                    if(num != null) {
                        nextID = (int) (long) (realm.where(Place.class).max("id")) + 1;
                    } else {
                        nextID = 0;
                    }
                    place.setId(nextID);
                    realm.copyToRealm(place);
                }
            }
        });
    }

    public void addSuggestedPlaceToDatabase(Place place) {

        final SuggestedPlace suggestedPlace = new SuggestedPlace();
        suggestedPlace.setLatitude(place.getLatitude());
        suggestedPlace.setLongitude(place.getLongitude());
        suggestedPlace.setId();
        suggestedPlace.setName(place.getName());

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                usersAllergenList = realm.where(Allergen.class).findAll();
            }
        });

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SuggestedPlace place = realm.where(SuggestedPlace.class).equalTo("id", suggestedPlace.getId()).findFirst();

                if (place != null) {
                    allAllergensOfSinglePlace = place.getUsersAllergensList();
                }

                Allergen allergen = new Allergen(context.getString(R.string.nothing), false);

                if (usersAllergenList.size() != 0) {
                    for (Allergen item : usersAllergenList) {
                        if (!allAllergensOfSinglePlace.contains(item)) {
                            allAllergensOfSinglePlace.add(item);
                        }
                    }
                } else if (!allAllergensOfSinglePlace.contains(allergen))
                    allAllergensOfSinglePlace.add(allergen);

            }
        });

        suggestedPlace.setUsersAllergensList(allAllergensOfSinglePlace);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(suggestedPlace);
            }
        });

    }

    public List<Place> getUsersFavouritePlacesList() {
        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                usersFavouritePlacesList = realm.where(Place.class).findAllSorted("name");
            }
        });
        return usersFavouritePlacesList;
    }

    public List<SuggestedPlace> getAllSuggestedPlacesList() {
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                suggestedPlacesList = realm.where(SuggestedPlace.class).findAllSorted("name");
            }
        });
        return suggestedPlacesList;
    }

    public List<SuggestedPlace> getSuggestedPlaces() {
        AllergenDao allergenDao = new AllergenDao();

        ArrayList<String> userAllergensNames = new ArrayList<>();

        List<SuggestedPlace> allSuggestedPlacesList;
        List<SuggestedPlace> userSuggestedPlacesList = new ArrayList<>();

        allSuggestedPlacesList = getAllSuggestedPlacesList();
        usersAllergenList = allergenDao.getMyAllergensList();

        for (Allergen item : usersAllergenList){
            userAllergensNames.add(item.getName());
        }

        for (SuggestedPlace place : allSuggestedPlacesList) {
            ArrayList<String> placeAllergensNames = new ArrayList<>();
            for (Allergen item : place.getUsersAllergensList()) {
                placeAllergensNames.add(item.getName());
            }

            if (placeAllergensNames.containsAll(userAllergensNames)) {
                userSuggestedPlacesList.add(place);
            }
        }

        return userSuggestedPlacesList;
    }

    public String[] getNearestFavouritePlaces(double longitude, double latitude){
        //1 stopień = 110,7km

        final double latitudeFrom = latitude - 0.001;
        final double latitudeTo = latitude + 0.001;
        final double longitudeFrom = longitude - 0.001;
        final double longitudeTo = longitude + 0.001;

        privateDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favouritePlacesList = realm.where(Place.class)
                        .beginGroup()
                        .between("longitude",longitudeFrom,longitudeTo)
                        .between("latitude", latitudeFrom,latitudeTo)
                        .endGroup()
                        .findAllSorted("name");
            }
        });

        String[] placesList = new String[favouritePlacesList.size()];
        for (int i = 0; i < favouritePlacesList.size(); i++){
            placesList[i] = favouritePlacesList.get(i).getName();
        }

    return placesList;
    }
}

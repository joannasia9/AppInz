package com.example.asia.jmpro.logic.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.asia.jmpro.MainMenuPlaces;
import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Place;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocationChangeObserver extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000; //miliseconds
    private static final float LOCATION_DISTANCE = 150f;


    private class LocationListener implements android.location.LocationListener
    {
        private Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location)
        {   mLastLocation.set(location);

//            if(privateDatabase != null){
//                privateDatabase.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        createBigNotification(getNearestPlaces(realm,location));
//                    }
//                });
//                Toast.makeText(getApplicationContext(), "Realm not null" + location.getLatitude(), Toast.LENGTH_LONG).show();
//            }


           //create notification
            Log.e(TAG, "onLocationChanged: " + location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }



    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
//        String fileName = intent.getExtras().getString("lastSignedInUsersName","default.realm");
//        long schemaVersion = intent.getExtras().getLong("realmSchemaVersion",1);
//
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name(fileName)
//                .schemaVersion(schemaVersion)
//                .modules(new PrivateEntitiesModule())
//                .build();
//
//        privateDatabase = Realm.getInstance(config);

        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    private String[] getNearestPlaces(Realm realm, Location location){
        final double latitudeFrom = location.getLatitude() - 0.002;
        final double latitudeTo = location.getLatitude() + 0.002;
        final double longitudeFrom = location.getLongitude() - 0.002;
        final double longitudeTo = location.getLongitude() + 0.002;
        RealmResults<Place> favouritePlacesList = realm.where(Place.class)
                .beginGroup()
                .between("longitude",longitudeFrom,longitudeTo)
                .between("latitude", latitudeFrom,latitudeTo)
                .endGroup()
                .findAll();

        String[] placesList = new String[favouritePlacesList.size()];
        for(int i = 0; i<favouritePlacesList.size(); i++){
            placesList[i]=favouritePlacesList.get(i).getName();
        }

        return placesList;
    }
    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

        protected void createBigNotification(String[] msgPositions) {
            Intent intent = new Intent(this, MainMenuPlaces.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Znajdujesz się w pobliżu ulubioych miejsc:");
            for (String msgPosition : msgPositions) {
                inboxStyle.addLine(msgPosition);
            }

            Notification noti = new NotificationCompat.Builder(this)
                    .setContentTitle("Nowa wiadomość")
                    .setContentText(msgPositions[0])
                    .setTicker("Masz wiadomość")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.item1))
                    .setAutoCancel(true)
                    .setContentIntent(pIntent)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, noti);
}
}

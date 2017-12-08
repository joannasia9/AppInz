package com.example.asia.jmpro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.example.asia.jmpro.MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION;

public class ManualLocationSetter extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    Marker marker;
    LatLng newPosition;
    PreferencesChangeObserver preferencesChangeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(getApplicationContext()));
        super.onCreate(savedInstanceState);
        preferencesChangeObserver = new PreferencesChangeObserver(this);
        setContentView(R.layout.activity_manual_location_setter);

        initMap();
    }

    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.manualLocationSetterMapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat, lng;
        lat = getCurrentLocation().getLatitude();
        lng = getCurrentLocation().getLongitude();

        map = googleMap;
        goToLocationZoom(getCurrentLocation());
        marker = map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng)));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                newPosition= new LatLng(latLng.latitude, latLng.longitude);

                String name = getCurrentPlaceAddress(latLng.latitude,latLng.longitude);
                if(marker!=null){
                    marker.remove();
                }

                marker = map.addMarker(new MarkerOptions()
                            .position(newPosition)
                            .title(name));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        });
    }

    private void showAddFavouritePlaceDialog(LatLng latLng) {
        Intent intent = new Intent();
        intent.putExtra("lat", latLng.latitude);
        intent.putExtra("lng", latLng.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }


    private String getCurrentPlaceAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> list;
        String localityName = "";

        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = list.get(0);

            localityName = getString(R.string.street) + address.getAddressLine(0) + ", \n" + address.getAddressLine(1);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return localityName;
    }


    private void goToLocationZoom(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8);
        map.moveCamera(cameraUpdate);

    }

    private Location getCurrentLocation() {
        Location location = new Location("");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if(locationManager != null) {
            String provider = locationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
            location = locationManager.getLastKnownLocation(provider);
        }
        return location;
    }


    public void choosePlace(View view) {
        showAddFavouritePlaceDialog(newPosition);
    }

}
